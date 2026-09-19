package com.trantanh.navipos.utils;

/**
 *
 * @author mickapa1
 */
public interface HashProvider {
    /**
     * Calculates hash of the given string
     * @param s string
     * @return hash(s)
     */
     String computeHash(String s);
}
