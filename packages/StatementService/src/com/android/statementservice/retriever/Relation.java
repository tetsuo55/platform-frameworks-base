/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.statementservice.retriever;

import android.annotation.NonNull;

import java.util.regex.Pattern;

/**
 * An immutable value type representing a statement relation with "kind" and "detail".
 *
 * <p> The set of kinds is enumerated by the API: <ul> <li> <b>delegate_permission</b>: The detail
 * field specifies which permission to delegate. A statement involving this relation does not
 * constitute a requirement to do the delegation, just a permission to do so. </ul>
 *
 * <p> We may add other kinds in the future.
 *
 * <p> The detail field is a lowercase alphanumeric string with underscores and periods allowed
 * (matching the regex [a-z0-9_.]+), but otherwise unstructured.
 */
public final class Relation {

    private static final Pattern KIND_PATTERN = Pattern.compile("^[a-z0-9_.]+$");
    private static final Pattern DETAIL_PATTERN = Pattern.compile("^([a-z0-9_.]+)$");

    private final String mKind;
    private final String mDetail;

    private Relation(String kind, String detail) {
        mKind = kind;
        mDetail = detail;
    }

    /**
     * Returns the relation's kind.
     */
    @NonNull
    public String getKind() {
        return mKind;
    }

    /**
     * Returns the relation's detail.
     */
    @NonNull
    public String getDetail() {
        return mDetail;
    }

    /**
     * Creates a new Relation object for the specified {@code kind} and {@code detail}.
     *
     * @throws AssociationServiceException if {@code kind} or {@code detail} is not well formatted.
     */
    public static Relation create(@NonNull String kind, @NonNull String detail)
            throws AssociationServiceException {
        if (!KIND_PATTERN.matcher(kind).matches() || !DETAIL_PATTERN.matcher(detail).matches()) {
            throw new AssociationServiceException("Relation not well formatted.");
        }
        return new Relation(kind, detail);
    }

    /**
     * Creates a new Relation object from its string representation.
     *
     * @throws AssociationServiceException if the relation is not well formatted.
     */
    public static Relation create(@NonNull String relation) throws AssociationServiceException {
        String[] r = relation.split("/", 2);
        if (r.length != 2) {
            throw new AssociationServiceException("Relation not well formatted.");
        }
        return create(r[0], r[1]);
    }

    /**
     * Returns true if {@code relation} has the same kind and detail.
     */
    public boolean matches(Relation relation) {
        return getKind().equals(relation.getKind()) && getDetail().equals(relation.getDetail());
    }

    /**
     * Returns a string representation of this relation.
     */
    @Override
    public String toString() {
        StringBuilder relation = new StringBuilder();
        relation.append(getKind());
        relation.append("/");
        relation.append(getDetail());
        return relation.toString();
    }

    // equals() and hashCode() are generated by Android Studio.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Relation relation = (Relation) o;

        if (mDetail != null ? !mDetail.equals(relation.mDetail) : relation.mDetail != null) {
            return false;
        }
        if (mKind != null ? !mKind.equals(relation.mKind) : relation.mKind != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = mKind != null ? mKind.hashCode() : 0;
        result = 31 * result + (mDetail != null ? mDetail.hashCode() : 0);
        return result;
    }
}
