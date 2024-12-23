CREATE TABLE TASK (
    ID bigint not null auto_increment,
    NAME varchar(50) not null,
    TEXT varchar(250) not null,
    CREATION_DATE timestamp,
    ENDING_DATE timestamp,
    COMPLETED bit,
    primary key (ID)
);