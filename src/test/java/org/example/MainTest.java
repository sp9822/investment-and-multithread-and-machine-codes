package org.example;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
    private Main main=new Main();

    private Integer method(){
        Integer ans=null;

        return ans;
    }

    @Test
    public void test(){
        Integer ans=method();
        assertEquals(null, ans);
    }
}
