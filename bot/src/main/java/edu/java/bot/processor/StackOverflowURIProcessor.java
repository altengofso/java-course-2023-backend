package edu.java.bot.processor;

public class StackOverflowURIProcessor extends URIProcessor {
    private static final String HOSTNAME = "stackoverflow.com";

    public StackOverflowURIProcessor(URIProcessor nextURIProcessor) {
        super(nextURIProcessor);
    }

    @Override
    protected String getValidHostName() {
        return HOSTNAME;
    }
}
