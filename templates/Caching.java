@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("examples");
    }
}

// Usage in Service
@Cacheable("examples")
public Example getExampleById(Long id) {
    return exampleRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
}
