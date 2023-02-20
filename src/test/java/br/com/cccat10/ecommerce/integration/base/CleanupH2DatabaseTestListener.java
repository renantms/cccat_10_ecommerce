package br.com.cccat10.ecommerce.integration.base;

import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.sql.SQLException;

public class CleanupH2DatabaseTestListener implements TestExecutionListener, Ordered {
    private static final String H2_SCHEMA_NAME = "PUBLIC";

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        TestExecutionListener.super.beforeTestMethod(testContext);
        cleanupDatabase(testContext);
    }

    private void cleanupDatabase(TestContext testContext) throws SQLException {
        CleanupH2DbService cleanupH2DbService = testContext.getApplicationContext().getBean(CleanupH2DbService.class);
        cleanupH2DbService.cleanup(H2_SCHEMA_NAME);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}