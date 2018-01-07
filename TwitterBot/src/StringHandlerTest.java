import org.junit.Assert;
import org.junit.Test;

public class StringHandlerTest {
	
	StringHandler str = new StringHandler("words.csv");


	@Test
	public void testInsertStringUsingChar() {
		Assert.assertEquals("Hello, World",str.insertString("Hello World", ",", 5));
	}
	@Test
	public void testInsertString() {
		Assert.assertEquals("Ansar Khan is the best",str.insertString("Ansar is the best" , " Khan", 5));
	}
	@Test
	public void testIsCaps() {
		Assert.assertEquals("Did You Mean... Merica", str.replace("America"));
	}

}
