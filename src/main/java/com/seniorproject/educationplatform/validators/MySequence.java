package com.seniorproject.educationplatform.validators;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, MySequence.First.class, MySequence.Second.class})
public interface MySequence {
    interface First {}
    interface Second {}
}

