//package com.resume.bot.json.entity.client;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonUnwrapped;
//import com.resume.bot.json.entity.common.Type;
//import lombok.*;
//
//import java.util.Arrays;
//
//@AllArgsConstructor
//@Data
//public class Contact {
//    private enum TypeContacts {
//        HOME,
//        WORK,
//        CELL,
//        EMAIL;
//
//        @Override
//        public String toString() {
//            return name().toLowerCase();
//        }
//    }
//
//    private String comment;
//
//    @JsonProperty("need_verification")
//    private Boolean needVerification;
//
//    private boolean preferred;
//
//    @NonNull
//    private Type type;
//
//    @JsonUnwrapped
//    private ValuePhoneOrEmail value;
//
//    private Boolean verified;
//
//    public String getActualValue() {
//        if (value != null && Arrays.stream(TypeContacts.values()).anyMatch(ct -> ct.toString().equals(type.getName()))) {
//            Phone phone = value.getPhone();
//            return isEmail()
//                    ? value.getEmail()
//                    : phone != null ? phone.getFormatted()
//                    : null;
//        }
//        return null;
//    }
//
//    public boolean isEmail() {
//        return TypeContacts.EMAIL.toString().equals(value.getEmail());
//    }
//}
