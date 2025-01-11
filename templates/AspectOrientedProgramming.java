@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.api.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Executing: " + joinPoint.getSignature());
    }

    @AfterReturning(pointcut = "execution(* com.example.api.service.*.*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        System.out.println("Executed: " + joinPoint.getSignature() + " with result: " + result);
    }
}
