-- Создание таблицы AircraftCheck
CREATE TABLE aircraft_check (
                                id BIGSERIAL PRIMARY KEY,
                                check_name VARCHAR(255) UNIQUE NOT NULL
);

-- Создание таблицы Airplane
CREATE TABLE airplane (
                          id BIGSERIAL PRIMARY KEY,
                          serial_number VARCHAR(255) UNIQUE NOT NULL,
                          model VARCHAR(255) NOT NULL,
                          year_of_release DATE NOT NULL
);

-- Создание таблицы PerformedWork
CREATE TABLE performed_work (
                                id BIGSERIAL PRIMARY KEY,
                                description TEXT NOT NULL,
                                completion_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                result VARCHAR(50) NOT NULL,
                                aircraft_check_id BIGINT NOT NULL,
                                airplane_id BIGINT NOT NULL,
                                engineer_id VARCHAR(255) NOT NULL,
                                created_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                last_modified_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                created_by VARCHAR(255) NOT NULL,
                                last_modified_by VARCHAR(255),
                                FOREIGN KEY (aircraft_check_id) REFERENCES aircraft_check(id) ON DELETE CASCADE,
                                FOREIGN KEY (airplane_id) REFERENCES airplane(id) ON DELETE CASCADE
);

-- Создание таблицы ScheduledCheck
CREATE TABLE scheduled_check (
                                 id BIGSERIAL PRIMARY KEY,
                                 type VARCHAR(50) NOT NULL,
                                 date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                 status VARCHAR(50) NOT NULL,
                                 notification_sent BOOLEAN NOT NULL,
                                 airplane_id BIGINT NOT NULL,
                                 engineer_id VARCHAR(255) NOT NULL,
                                 created_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 last_modified_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                 created_by VARCHAR(255) NOT NULL,
                                 last_modified_by VARCHAR(255),
                                 FOREIGN KEY (airplane_id) REFERENCES airplane(id) ON DELETE CASCADE
);

-- Создание таблицы Notification
CREATE TABLE notification (
                              id BIGSERIAL PRIMARY KEY,
                              serial_number VARCHAR(255),
                              type VARCHAR(50) NOT NULL,
                              date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                              created_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              last_modified_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Триггер для обновления last_modified_date в таблице Notification
CREATE OR REPLACE FUNCTION update_last_modified_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.last_modified_date = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_notification_last_modified
    BEFORE UPDATE ON notification
    FOR EACH ROW
EXECUTE FUNCTION update_last_modified_column();

-- Триггер для обновления last_modified_date в таблице PerformedWork
CREATE OR REPLACE FUNCTION update_performed_work_last_modified_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.last_modified_date = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_performed_work_last_modified
    BEFORE UPDATE ON performed_work
    FOR EACH ROW
EXECUTE FUNCTION update_performed_work_last_modified_column();

-- Триггер для обновления last_modified_date в таблице ScheduledCheck
CREATE OR REPLACE FUNCTION update_scheduled_check_last_modified_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.last_modified_date = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_scheduled_check_last_modified
    BEFORE UPDATE ON scheduled_check
    FOR EACH ROW
EXECUTE FUNCTION update_scheduled_check_last_modified_column();