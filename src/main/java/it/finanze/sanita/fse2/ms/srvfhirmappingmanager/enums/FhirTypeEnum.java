/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums;

public enum FhirTypeEnum {

    Map("Map"),
    Definition("Definition"),
    Valueset("Valueset");

    private final String name;

    FhirTypeEnum(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
