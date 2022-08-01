package com.example.social_network_fpt_be.util;

public enum ImageType {
    USER_AVT {
        public String toString() {
            return "user_avt";
        }
    },
    USER_COVER {
        public String toString() {
            return "user_cover";
        }
    },
    POST_IMAGE {
        @Override
        public String toString() {
            return "post_image";
        }
    },
    COMMENT_IMAGE {
        @Override
        public String toString() {
            return "comment_image";
        }
    }
}
