package edu.java.bot.processor;

public class GitHubURIProcessor extends URIProcessor {
    private static final String HOSTNAME = "github.com";

    public GitHubURIProcessor(URIProcessor nextURIProcessor) {
        super(nextURIProcessor);
    }

    @Override
    protected String getValidHostName() {
        return HOSTNAME;
    }
}
