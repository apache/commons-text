/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.names;

import java.util.Objects;

/**
 * An object representing the result of parsing a Name.
 *
 * <p>This class is immutable.</p>
 */
public final class Name {

    private final String leadingInitial;
    private final String salutation;
    private final String firstName;
    private final String nickName;
    private final String middleName;
    private final String lastName;
    private final String suffix;

    public Name(String leadingInitial, String salutation, String firstName, String nickName, String middleName, String lastName, String suffix) {
        this.leadingInitial = leadingInitial;
        this.salutation = salutation;
        this.firstName = firstName;
        this.nickName = nickName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.suffix = suffix;
    }

    // TODO Add an example to each getter

    /**
     * Gets the leading init part of the name.
     *
     * @return the leading init part of the name
     */
    public String getLeadingInitial() {
        return leadingInitial;
    }


    public String getSalutation() {
        return salutation;
    }

    /**
     * Gets the first name.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the nickname.
     *
     * @return the nickname
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Gets the middle name.
     *
     * @return the middle name
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the suffix part of the name.
     *
     * @return the name suffix
     */
    public String getSuffix() {
        return suffix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(leadingInitial, name.leadingInitial) &&
                Objects.equals(firstName, name.firstName) &&
                Objects.equals(nickName, name.nickName) &&
                Objects.equals(middleName, name.middleName) &&
                Objects.equals(lastName, name.lastName) &&
                Objects.equals(suffix, name.suffix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leadingInitial, firstName, nickName, middleName, lastName, suffix);
    }
}
