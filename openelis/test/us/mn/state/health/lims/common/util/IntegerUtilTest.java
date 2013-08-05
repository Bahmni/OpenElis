package us.mn.state.health.lims.common.util;


import junit.framework.Assert;
import org.junit.Test;

public class IntegerUtilTest {

    @Test
    public void testGetParsedValueOrDefault() {
        Assert.assertEquals((int)IntegerUtil.getParsedValueOrDefault("10", 0), 10);

        Assert.assertEquals((int)IntegerUtil.getParsedValueOrDefault("", 0), 0);
        Assert.assertEquals((int)IntegerUtil.getParsedValueOrDefault("", 100), 100);

        Assert.assertEquals((int)IntegerUtil.getParsedValueOrDefault("gibberish", 0), 0);
        Assert.assertEquals((int)IntegerUtil.getParsedValueOrDefault("gibberish", 100), 100);
    }
}
