package edu.java.scrapper.controller;

import edu.java.scrapper.controller.dto.ApiErrorResponse;
import edu.java.scrapper.service.jdbc.JdbcTgChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class TgChatController {
    private final JdbcTgChatService tgChatService;

    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат зарегистрирован"),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Чат уже зарегистрирован",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @PostMapping(
        path = "/tg-chat/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> registerChat(@PathVariable(name = "id") long id) {
        tgChatService.registerChat(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат успешно удалён"),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Чат не существует",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable(name = "id") long id) {
        tgChatService.deleteChat(id);
        return ResponseEntity.ok().build();
    }
}
