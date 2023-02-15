/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transform Body for insert and update endpoints
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransformBodyDTO {
    private List<String> templateIdRoot;
    private String version;
}
