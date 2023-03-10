package com.tcf.sma.Survey.httpServices;

public class HttpEventsArgs {

    private final IHttpPayloadRequestEventsHandler requestSender;
    private final IHttpPayloadService asyncServiceObject;
    private final Object tagData;
    private final Object eventData;

    public HttpEventsArgs(IHttpPayloadRequestEventsHandler sender, IHttpPayloadService service, Object tag, Object data) {
        requestSender = sender;
        asyncServiceObject = service;
        tagData = tag;
        eventData = data;
    }

    public IHttpPayloadRequestEventsHandler getSender() {
        return requestSender;
    }

    public IHttpPayloadService getServiceInstance() {
        return asyncServiceObject;
    }

    public Object getTag() {
        return tagData;
    }

    public Object getEventData() {
        return eventData;
    }
}
