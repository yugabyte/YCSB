package site.ycsb.airwallex.workloads;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import site.ycsb.ByteIterator;
import site.ycsb.StringByteIterator;
import site.ycsb.WorkloadException;
import site.ycsb.workloads.CoreWorkload;

/**
 * Load workload from a template defined in resources folder.
 */
public class TemplatedWorkload extends CoreWorkload {
  public static final String TABLENAME_PROPERTY_DEFAULT = "account";
  public static final String DATA_TEMPLATE_NAME_DEFAULT = "1record_33KB.json";
  public static final String FIELD_NAME_ID = "id";
  public static final String FIELD_NAME_DATA = "data";
  private Map<String, String> templateData;

  @Override
  public void init(Properties p) throws WorkloadException {
    super.init(p);
    table = TABLENAME_PROPERTY_DEFAULT;
    try {
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
      InputStream is = classloader.getResourceAsStream(DATA_TEMPLATE_NAME_DEFAULT);
      String result = IOUtils.toString(is, StandardCharsets.UTF_8.toString());
      templateData = new ObjectMapper().readValue(result, HashMap.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected String buildKeyName(long keynum) {
    return UUID.randomUUID().toString();
  }
  @Override
  protected HashMap<String, ByteIterator> buildValues(String key) {
    HashMap<String, ByteIterator> values = new HashMap<>();
    values.put(FIELD_NAME_DATA, new StringByteIterator(templateData.get(FIELD_NAME_DATA)));
    return values;
  }
}
