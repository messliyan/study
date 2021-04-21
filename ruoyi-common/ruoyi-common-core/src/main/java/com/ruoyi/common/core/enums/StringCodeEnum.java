package com.ruoyi.common.core.enums;

public interface StringCodeEnum extends CodeEnum {

  /**
   * Returns the underlying code associated to this enum type. Sub-class (the actual Enum type)
   * should return associated code value stored in DB.
   *
   * @return the code value of this type
   */
  String getCode();

  /**
   * Returns the description of this code.
   *
   * @return the description of this code.
   */
  String getDescription();

  /**
   * Returns the enum of the given enum type class and the given code. This is enhanced version of
   * looking up of {@link Enum#valueOf(Class, String)}, which is looking up enum by its name
   *
   * @param cType the enum type class
   * @param code  the enum code value
   * @param <E>   the enum type
   * @return the enum representation of the given type and code
   */
  default <E extends StringCodeEnum> E getEnumForCode(Class<E> cType,
      String code) {
    if (!cType.isEnum()) {
      throw new IllegalArgumentException(
          "Class " + cType.getCanonicalName() + " is not a enum.");
    }
    E[] enumType = cType.getEnumConstants();
    for (E e : enumType) {
      if (e.getCode().equals(code)) {
        return e;
      }
    }
    throw new IllegalArgumentException(
        "No enum constant for code " + code + " for enum type " +
            cType.getCanonicalName());
  }

  default <E extends StringCodeEnum> E getEnumForDescription(Class<E> cType,
      String description) {
    if (!cType.isEnum()) {
      throw new IllegalArgumentException(
          "Class " + cType.getCanonicalName() + " is not a enum.");
    }
    E[] enumType = cType.getEnumConstants();
    String des = description;
    for (E e : enumType) {
      if ((e.getDescription().trim()).equals(des.trim())) {
        return e;
      }
    }
    throw new IllegalArgumentException(
        "No enum constant for description " + description + " for enum type " +
            cType.getCanonicalName());
  }

  /**
   * Same as {@link Enum#valueOf(Class, String)}, linked here for completeness
   *
   * @param <E>   The enum type whose constant is to be returned
   * @param cType the {@code Class} object of the enum type from which to return a constant
   * @param name  the name of the constant to return
   * @return the enum constant of the specified enum type with the specified name
   */
  default <E extends Enum<E>> E getEnumForName(Class<E> cType, String name) {
    return E.valueOf(cType, name);
  }

}
