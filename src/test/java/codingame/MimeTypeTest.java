package codingame;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by swu on 23/02/2017.
 */
public class MimeTypeTest extends TestCase {
  @Test
  public void testMatches() {
    String fileName = "animated.gif";
    assert fileName.matches("." + "gif" + "$");
  }
}
