package com.CardManagement.card.Enum;

public class CardDetails {

    public enum Gender {
        MALE,
        FEMALE,
        THIRDGENDER
    }
    public enum MaritalStatus {
        SINGLE,
        MARRIED,
        OTHERS
    }

    public enum EducationalQualification {
        POSTGRADUATE,
        GRADUATE,
        UNDERGRADUATE,
        OTHERS
    }

    public enum OccupationalType {
        SALARIED,
        SELF_EMPLOYED,
        STUDENT,
        RETIRED,
        BUSINESS,
        OTHERS
    }

    public enum AddressType {
        PERMANENT,
        COMMUNICATION,
        TEMPORARY,
        OFFICE
    }

    public enum CustomHttpStatus {
        SUCCESS
    }

    public enum PaymentType {
        DEBIT,
        CREDIT
    }
}
