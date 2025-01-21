package com.sharafutdinov.aircraft_maintenance.enums;

import lombok.Getter;

@Getter
public enum EmailTemplates {

    SCHEDULED_TASK("ScheduledTask.html", "ЗАПЛАНИРОВАННАЯ РАБОТА"),
    UPDATED_SCHEDULED_TASK("UpdatedScheduledTask.html", "ВРЕМЯ РАБОТЫ БЫЛО ИЗМЕНЕНО")
    ;

    private final String template;
    private final String subject;

    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
