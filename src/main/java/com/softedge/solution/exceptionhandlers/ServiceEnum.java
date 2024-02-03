package com.softedge.solution.exceptionhandlers;

import java.util.EnumSet;
import java.util.Iterator;

public enum ServiceEnum {

    FILEGENERATOR(1, "fileservice");

    private int serviceId;
    private String serviceEnumName;

    private ServiceEnum(int serviceId, String serviceEnumName) {
        this.serviceId = serviceId;
        this.serviceEnumName = serviceEnumName;
    }

    public int getServiceId() {
        return this.serviceId;
    }

    public String getServiceEnumName() {
        return this.serviceEnumName;
    }

    public static BaseException createServiceException(ServiceError serviceError, ErrorCodeEnum erorData) {
        return serviceError.getHttpStatus() == 400 && serviceError.getHttpStatus() == 404 ? new CertusException(erorData, serviceError.getMessage(), serviceError.getMessageArgs()) : new CertusException(erorData, serviceError.getMessage(), serviceError.getMessageArgs());
    }

    static ServiceEnum getServiceId(String serviceName) {
        Iterator var1 = EnumSet.allOf(ServiceEnum.class).iterator();

        ServiceEnum e;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            e = (ServiceEnum) var1.next();
        } while (!e.getServiceEnumName().equalsIgnoreCase(serviceName));

        return e;
    }
}
