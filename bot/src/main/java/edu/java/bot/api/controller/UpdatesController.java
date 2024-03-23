package edu.java.bot.api.controller;

import edu.java.bot.api.models.ApiErrorResponse;
import edu.java.bot.api.models.LinkUpdate;
import edu.java.bot.api.service.UpdatesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@Validated
public class UpdatesController {
    private final UpdatesService updatesService;

    @Operation(summary = "Отправить обновление")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Обновление обработано"),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @PostMapping(
        path = "/updates",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> sendUpdates(@RequestBody LinkUpdate linkUpdate) {
        log.info("Sending update {}", linkUpdate);
        updatesService.sendUpdates(linkUpdate);
        return ResponseEntity.ok().build();
    }
}
