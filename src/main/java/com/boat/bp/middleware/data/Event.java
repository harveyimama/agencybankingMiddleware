package com.boat.bp.middleware.data;

public class Event {

    private String actionName;
    private String entityName;

    public Event(final String actionName, final String entityName)
    {
       this.actionName = actionName;
       this.entityName = entityName;
    }

    public String getActionName() {
        return actionName;
    }
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    public String getEntityName() {
        return entityName;
    }
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    
}
