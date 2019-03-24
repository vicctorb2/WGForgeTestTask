package com.vicctorb.wgtesttask36.validation;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class SortingAndPagingRequest {

    @Pattern(regexp = "^(name|color|tail_length|whiskers_length)$", message = "Error attribute value. It can be 'color', 'name', 'tail_length' or 'whiskers_length\n")
    String attribute;

    @Pattern(regexp = "^(asc|desc)$", message = "Error 'order' value.'Order' parameter value can be only 'desc' or 'asc'\n")
    String order;

    @PositiveOrZero(message = "Incorrect 'offset' value. 'Offset' value must be positive or zero\n")
    Integer offset;

    @Positive(message = "Incorrect 'limit' value. 'Limit' value must be positive\n")
    Integer limit;

    public SortingAndPagingRequest() {
    }

    public SortingAndPagingRequest(@Pattern(regexp = "^(name|color|tail_length|whiskers_length)$", message = "Error attribute value. It can be 'color', 'name', 'tail_length' or 'whiskers_length\n") String attribute, @Pattern(regexp = "^(asc|desc)$", message = "Error 'order' value.'Order' parameter value can be only 'desc' or 'asc'\n") String order, @PositiveOrZero(message = "Incorrect 'offset' value. 'Offset' value must be positive or zero\n") Integer offset, @Positive(message = "Incorrect 'limit' value. 'Limit' value must be positive\n") Integer limit) {
        this.attribute = attribute;
        this.order = order;
        this.offset = offset;
        this.limit = limit;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
