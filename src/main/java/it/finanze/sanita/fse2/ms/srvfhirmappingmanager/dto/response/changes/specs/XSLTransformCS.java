/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.specs;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

@Data
@AllArgsConstructor
public class XSLTransformCS {
    @Size(max = DEFAULT_STRING_MAX_SIZE)
    String templateIdRoot;
    @Size(max = DEFAULT_STRING_MAX_SIZE)
    String version;
}
