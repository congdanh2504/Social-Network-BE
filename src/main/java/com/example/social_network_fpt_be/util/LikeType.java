package com.example.social_network_fpt_be.util;

public enum LikeType {
    POST {
        @Override
        public String toString() {
            return "post";
        }
    },
    COMMENT {
        @Override
        public String toString() {
            return "comment";
        }
    }
}
