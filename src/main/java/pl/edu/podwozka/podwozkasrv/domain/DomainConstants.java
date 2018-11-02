package pl.edu.podwozka.podwozkasrv.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class DomainConstants {

    public static boolean getEqualsMethodWithoutMetadataColumns(Object org, Object o) {
        return EqualsBuilder.reflectionEquals(org, o,
                "id",
                "createdBy",
                "createdDate",
                "lastModifiedBy",
                "lastModifiedDate");
    }

    public static int getHashCodeMethodWithoutMetadataColumns(Object org) {
        return HashCodeBuilder.reflectionHashCode(org,
                "id",
                "createdBy",
                "createdDate",
                "lastModifiedBy",
                "lastModifiedDate");
    }

    private DomainConstants() {
    }
}
