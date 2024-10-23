import java.nio.file.Path

def separator = File.separator
def parentPom = Path.of(request.getOutputDirectory() + separator + request.getArtifactId() + separator + "pom.xml")
        .toFile()

def lines = parentPom.readLines()
lines.removeIf {it.matches("(?m)^\\s*\$")}
def text = lines.join(System.getProperty("line.separator"))
parentPom.write(text)