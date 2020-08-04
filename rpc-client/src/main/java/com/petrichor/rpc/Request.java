package com.petrichor.rpc;

import javafx.beans.NamedArg;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
/**
 * @author petrichor
 * @date 2020/8/3 22:19
 */
@Builder
@Data
public class Request implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private String requestId;
    private String InterfaceName;
    private String menthodName;
    private Object[] parameters;
    //一个Class实例包含了该class的所有完整信息：
    private Class<?>[] paramTypes;
}
