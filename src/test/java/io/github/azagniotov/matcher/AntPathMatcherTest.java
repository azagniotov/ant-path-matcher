package io.github.azagniotov.matcher;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Part of these test case have been kindly borrowed from https://github.com/spring-projects/spring-framework:
 * ../org/springframework/util/AntPathMatcherTests.java
 */
public class AntPathMatcherTest {

    @Test
    public void isMatchWithCaseSensitiveWithDefaultPathSeparator() throws Exception {

        final AntPathMatcher pathMatcher = new AntPathMatcher.Builder().build();

        // test exact matching
        assertTrue(pathMatcher.isMatch("test", "test"));
        assertTrue(pathMatcher.isMatch("/test", "/test"));
        assertTrue(pathMatcher.isMatch("http://example.org", "http://example.org")); // SPR-14141
        assertFalse(pathMatcher.isMatch("/test.jpg", "test.jpg"));
        assertFalse(pathMatcher.isMatch("test", "/test"));
        assertFalse(pathMatcher.isMatch("/test", "test"));

        // test matching with ?'s
        assertTrue(pathMatcher.isMatch("t?st", "test"));
        assertTrue(pathMatcher.isMatch("??st", "test"));
        assertTrue(pathMatcher.isMatch("tes?", "test"));
        assertTrue(pathMatcher.isMatch("te??", "test"));
        assertTrue(pathMatcher.isMatch("?es?", "test"));
        assertFalse(pathMatcher.isMatch("tes?", "tes"));
        assertFalse(pathMatcher.isMatch("tes?", "testt"));
        assertFalse(pathMatcher.isMatch("tes?", "tsst"));

        // test matching with *'s
        assertTrue(pathMatcher.isMatch("*", "test"));
        assertTrue(pathMatcher.isMatch("test*", "test"));
        assertTrue(pathMatcher.isMatch("test*", "testTest"));
        assertTrue(pathMatcher.isMatch("test/*", "test/Test"));
        assertTrue(pathMatcher.isMatch("test/*", "test/t"));
        assertTrue(pathMatcher.isMatch("test/*", "test/"));
        assertTrue(pathMatcher.isMatch("*test*", "AnothertestTest"));
        assertTrue(pathMatcher.isMatch("*test", "Anothertest"));
        assertTrue(pathMatcher.isMatch("*.*", "test."));
        assertTrue(pathMatcher.isMatch("*.*", "test.test"));
        assertTrue(pathMatcher.isMatch("*.*", "test.test.test"));
        assertTrue(pathMatcher.isMatch("test*aaa", "testblaaaa"));
        assertFalse(pathMatcher.isMatch("test*", "tst"));
        assertFalse(pathMatcher.isMatch("test*", "tsttest"));
        assertFalse(pathMatcher.isMatch("test*", "test/"));
        assertFalse(pathMatcher.isMatch("test*", "test/t"));
        assertFalse(pathMatcher.isMatch("test/*", "test"));
        assertFalse(pathMatcher.isMatch("*test*", "tsttst"));
        assertFalse(pathMatcher.isMatch("*test", "tsttst"));
        assertFalse(pathMatcher.isMatch("*.*", "tsttst"));
        assertFalse(pathMatcher.isMatch("test*aaa", "test"));
        assertFalse(pathMatcher.isMatch("test*aaa", "testblaaab"));

        // test matching with ?'s and /'s
        assertTrue(pathMatcher.isMatch("/?", "/a"));
        assertTrue(pathMatcher.isMatch("/?/a", "/a/a"));
        assertTrue(pathMatcher.isMatch("/a/?", "/a/b"));
        assertTrue(pathMatcher.isMatch("/??/a", "/aa/a"));
        assertTrue(pathMatcher.isMatch("/a/??", "/a/bb"));
        assertTrue(pathMatcher.isMatch("/?", "/a"));

        // test matching with **'s
        assertTrue(pathMatcher.isMatch("/**", "/testing/testing"));
        assertTrue(pathMatcher.isMatch("/*/**", "/testing/testing"));
        assertTrue(pathMatcher.isMatch("/**/*", "/testing/testing"));
        assertTrue(pathMatcher.isMatch("/bla/**/bla", "/bla/testing/testing/bla"));
        assertTrue(pathMatcher.isMatch("/bla/**/bla", "/bla/testing/testing/bla/bla"));
        assertTrue(pathMatcher.isMatch("/**/test", "/bla/bla/test"));
        assertTrue(pathMatcher.isMatch("/bla/**/**/bla", "/bla/bla/bla/bla/bla/bla"));
        assertTrue(pathMatcher.isMatch("/bla*bla/test", "/blaXXXbla/test"));
        assertTrue(pathMatcher.isMatch("/*bla/test", "/XXXbla/test"));
        assertFalse(pathMatcher.isMatch("/bla*bla/test", "/blaXXXbl/test"));
        assertFalse(pathMatcher.isMatch("/*bla/test", "XXXblab/test"));
        assertFalse(pathMatcher.isMatch("/*bla/test", "XXXbl/test"));

        assertFalse(pathMatcher.isMatch("/????", "/bala/bla"));
        assertFalse(pathMatcher.isMatch("/**/*bla", "/bla/bla/bla/bbb"));

        assertTrue(pathMatcher.isMatch("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing/"));
        assertTrue(pathMatcher.isMatch("/*bla*/**/bla/*", "/XXXblaXXXX/testing/testing/bla/testing"));
        assertTrue(pathMatcher.isMatch("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing"));
        assertTrue(pathMatcher.isMatch("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing.jpg"));

        assertTrue(pathMatcher.isMatch("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing/"));
        assertTrue(pathMatcher.isMatch("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing"));
        assertTrue(pathMatcher.isMatch("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing"));
        assertFalse(pathMatcher.isMatch("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing/testing"));

        assertFalse(pathMatcher.isMatch("/x/x/**/bla", "/x/x/x/"));

        assertTrue(pathMatcher.isMatch("/foo/bar/**", "/foo/bar")) ;

        assertTrue(pathMatcher.isMatch("", ""));

        assertTrue(pathMatcher.isMatch("/foo/bar/**", "/foo/bar")) ;
        assertTrue(pathMatcher.isMatch("/resource/1", "/resource/1"));
        assertTrue(pathMatcher.isMatch("/resource/*", "/resource/1"));
        assertTrue(pathMatcher.isMatch("/resource/*/", "/resource/1/"));
        assertTrue(pathMatcher.isMatch("/top-resource/*/resource/*/sub-resource/*", "/top-resource/1/resource/2/sub-resource/3"));
        assertTrue(pathMatcher.isMatch("/top-resource/*/resource/*/sub-resource/*", "/top-resource/999999/resource/8888888/sub-resource/77777777"));
        assertTrue(pathMatcher.isMatch("/*/*/*/*/secret.html", "/this/is/protected/path/secret.html"));
        assertTrue(pathMatcher.isMatch("/*/*/*/*/*.html", "/this/is/protected/path/secret.html"));
        assertTrue(pathMatcher.isMatch("/*/*/*/*", "/this/is/protected/path"));
        assertTrue(pathMatcher.isMatch("org/springframework/**/*.jsp", "org/springframework/web/views/hello.jsp"));
        assertTrue(pathMatcher.isMatch("org/springframework/**/*.jsp", "org/springframework/web/default.jsp"));
        assertTrue(pathMatcher.isMatch("org/springframework/**/*.jsp", "org/springframework/default.jsp"));
        assertTrue(pathMatcher.isMatch("org/**/servlet/bla.jsp", "org/springframework/servlet/bla.jsp"));
        assertTrue(pathMatcher.isMatch("org/**/servlet/bla.jsp", "org/springframework/testing/servlet/bla.jsp"));
        assertTrue(pathMatcher.isMatch("org/**/servlet/bla.jsp", "org/servlet/bla.jsp"));
    }

    @Test
    public void isMatchWithCustomSeparator() throws Exception {
        final AntPathMatcher pathMatcher = new AntPathMatcher.Builder().withPathSeparator('.').build();

        assertTrue(pathMatcher.isMatch(".foo.bar.**", ".foo.bar")) ;
        assertTrue(pathMatcher.isMatch(".resource.1", ".resource.1"));
        assertTrue(pathMatcher.isMatch(".resource.*", ".resource.1"));
        assertTrue(pathMatcher.isMatch(".resource.*.", ".resource.1."));
        assertTrue(pathMatcher.isMatch("org.springframework.**.*.jsp", "org.springframework.web.views.hello.jsp"));
        assertTrue(pathMatcher.isMatch("org.springframework.**.*.jsp", "org.springframework.web.default.jsp"));
        assertTrue(pathMatcher.isMatch("org.springframework.**.*.jsp", "org.springframework.default.jsp"));
        assertTrue(pathMatcher.isMatch("org.**.servlet.bla.jsp", "org.springframework.servlet.bla.jsp"));
        assertTrue(pathMatcher.isMatch("org.**.servlet.bla.jsp", "org.springframework.testing.servlet.bla.jsp"));
        assertTrue(pathMatcher.isMatch("org.**.servlet.bla.jsp", "org.servlet.bla.jsp"));
        assertTrue(pathMatcher.isMatch("http://example.org", "http://example.org"));

        // test matching with ?'s and .'s
        assertTrue(pathMatcher.isMatch(".?", ".a"));
        assertTrue(pathMatcher.isMatch(".?.a", ".a.a"));
        assertTrue(pathMatcher.isMatch(".a.?", ".a.b"));
        assertTrue(pathMatcher.isMatch(".??.a", ".aa.a"));
        assertTrue(pathMatcher.isMatch(".a.??", ".a.bb"));
        assertTrue(pathMatcher.isMatch(".?", ".a"));

        // test matching with **'s
        assertTrue(pathMatcher.isMatch(".**", ".testing.testing"));
        assertTrue(pathMatcher.isMatch(".*.**", ".testing.testing"));
        assertTrue(pathMatcher.isMatch(".**.*", ".testing.testing"));
        assertTrue(pathMatcher.isMatch(".bla.**.bla", ".bla.testing.testing.bla"));
        assertTrue(pathMatcher.isMatch(".bla.**.bla", ".bla.testing.testing.bla.bla"));
        assertTrue(pathMatcher.isMatch(".**.test", ".bla.bla.test"));
        assertTrue(pathMatcher.isMatch(".bla.**.**.bla", ".bla.bla.bla.bla.bla.bla"));
        assertFalse(pathMatcher.isMatch(".bla*bla.test", ".blaXXXbl.test"));
        assertFalse(pathMatcher.isMatch(".*bla.test", "XXXblab.test"));
        assertFalse(pathMatcher.isMatch(".*bla.test", "XXXbl.test"));
    }

    @Test
    public void isMatchWithIgnoreCase() throws Exception {
        final AntPathMatcher pathMatcher = new AntPathMatcher.Builder().withIgnoreCase(true).build();

        assertTrue(pathMatcher.isMatch("/foo/bar/**", "/FoO/baR")) ;
        assertTrue(pathMatcher.isMatch("org/springframework/**/*.jsp", "ORG/SpringFramework/web/views/hello.JSP"));
        assertTrue(pathMatcher.isMatch("org/**/servlet/bla.jsp", "Org/SERVLET/bla.jsp"));
        assertTrue(pathMatcher.isMatch("/?", "/A"));
        assertTrue(pathMatcher.isMatch("/?/a", "/a/A"));
        assertTrue(pathMatcher.isMatch("/a/??", "/a/Bb"));
        assertTrue(pathMatcher.isMatch("/?", "/a"));
        assertTrue(pathMatcher.isMatch("/**", "/testing/teSting"));
        assertTrue(pathMatcher.isMatch("/*/**", "/testing/testing"));
        assertTrue(pathMatcher.isMatch("/**/*", "/tEsting/testinG"));
        assertTrue(pathMatcher.isMatch("http://example.org", "HtTp://exAmple.org"));
        assertTrue(pathMatcher.isMatch("HTTP://EXAMPLE.ORG", "HtTp://exAmple.org"));
    }

    @Test
    public void isMatchWithIgnoreCaseWithCustomPathSeparator() throws Exception {
        final AntPathMatcher pathMatcher = new AntPathMatcher.Builder()
                .withIgnoreCase(true)
                .withPathSeparator('.').build();

        assertTrue(pathMatcher.isMatch(".foo.bar.**", ".FoO.baR")) ;
        assertTrue(pathMatcher.isMatch("org.springframework.**.*.jsp", "ORG.SpringFramework.web.views.hello.JSP"));
        assertTrue(pathMatcher.isMatch("org.**.servlet.bla.jsp", "Org.SERVLET.bla.jsp"));
        assertTrue(pathMatcher.isMatch(".?", ".A"));
        assertTrue(pathMatcher.isMatch(".?.a", ".a.A"));
        assertTrue(pathMatcher.isMatch(".a.??", ".a.Bb"));
        assertTrue(pathMatcher.isMatch(".?", ".a"));
        assertTrue(pathMatcher.isMatch(".**", ".testing.teSting"));
        assertTrue(pathMatcher.isMatch(".*.**", ".testing.testing"));
        assertTrue(pathMatcher.isMatch(".**.*", ".tEsting.testinG"));
        assertTrue(pathMatcher.isMatch("http:..example.org", "HtTp:..exAmple.org"));
        assertTrue(pathMatcher.isMatch("HTTP:..EXAMPLE.ORG", "HtTp:..exAmple.org"));
    }
}
