package io.github.hison.api.caching;

/** 
 * @author Hani son
 * @version 1.0.4
 */
public class CachingHandlerFactory {
    private static CachingHandler cachingHandler;
    
    public static CachingHandler getHandler() {
        if (cachingHandler != null) {
            return cachingHandler;
        }
        return new CachingHandlerDefault();
    }

    public static void setCustomHandler(CachingHandler handler) {
        cachingHandler = handler;
    }
}
