package com.cocroachden;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class AbstractSpringBootContextTest extends AbstractApprovalTest {
}
