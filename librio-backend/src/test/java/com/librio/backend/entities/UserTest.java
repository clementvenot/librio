
package com.librio.backend.entities;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void equals_should_compare_email() {
        User u1 = new User();
        u1.setEmail("toto@mail.com");
        User u2 = new User();
        u2.setEmail("toto@mail.com");

        assertThat(u1).isEqualTo(u2);
    }

    @Test
    void hashCode_should_be_same_for_same_email() {
        User u1 = new User();
        u1.setEmail("toto@mail.com");
        User u2 = new User();
        u2.setEmail("toto@mail.com");
        
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
    }

}
