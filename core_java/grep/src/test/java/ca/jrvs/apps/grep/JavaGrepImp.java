package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep {

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  private String regex;
  private String rootPath;
  private String outFile;

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    //Use default logger config
    BasicConfigurator.configure();

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      javaGrepImp.logger.error(ex.getMessage(), ex);
    }
  }

  @Override
  public void process() throws IOException {
    String rootPath = getRootPath();
    listFiles(rootPath);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    try (Stream<Path> walk = Files.walk(Paths.get(rootDir))){
      List<File> result = walk.filter(Files::isRegularFile)
          .map(x -> x.toString()) //convert all files to string
          .map(x -> new File(x)) //typecast to File
          .collect(Collectors.toList()); //collect as list
      return result;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<String> readLines(File inputFile) {
    return null;
  }

  @Override
  public boolean containsPattern(String line) {
    String regex = getRegex();
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(line);
    boolean matchFound = matcher.find();
    return matchFound;
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {

  }

  @Override
  public String getRootPath() {
    return this.rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getRegex() {
    return this.regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return this.outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }
}
