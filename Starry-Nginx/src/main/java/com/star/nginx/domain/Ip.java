package com.star.nginx.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 05-16-2021 09:43
 */
@Data
public class Ip {

    public @interface AddIps {
    }

    public @interface DeleteIps {
    }

    @NotNull(groups = {AddIps.class})
    private List<String> serverIps;

    @NotNull(groups = {DeleteIps.class})
    private Boolean deleteAllFlag;

}
