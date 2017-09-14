package org.robertux.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by robertux on 9/13/17.
 */
@Data
@AllArgsConstructor
public class DropboxAccount {
    private String id;
    private String name;
    private String email;
    private String profileURL;

}
