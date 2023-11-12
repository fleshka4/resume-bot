package com.resume.latex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import static com.resume.util.FileLoader.loadFileContent;

public class LatexProcessor {

    private String latexSource;

    // if not empty, latexSource would be replaced
    public void addSource(String jsonString, TemplateNumber number) throws JsonProcessingException { //Client
        //map with path - templateNumber, path
        latexSource = loadFileContent("temp.tex");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        insertValue(Placeholder.PLACE_FOR_NAME, jsonNode.get("firstName").textValue() + " " + jsonNode.get("lastName").textValue());
        insertValue(Placeholder.PLACE_FOR_POSITION, jsonNode.get("title").textValue());

        /* figure how to insert contacts properly
        insertValue(Placeholder.PLACE_FOR_NUMBER, jsonNode.get("contact").textValue());
        insertValue(Placeholder.PLACE_FOR_EMAIL, jsonNode.get("contact").textValue());
        */
        /* put city in json before calling this function? country need? I guess, yes, but there are more problems
        insertValue(Placeholder.PLACE_FOR_CITY, jsonNode.get("areas???").textValue());
        */
        //etc
    }

    private void insertValue(Placeholder placeholder, String value) {
        if (!latexSource.contains(placeholder.toString())) {
            throw new RuntimeException("There is no " + placeholder);
        }
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Value for " + placeholder + " is empty");
        }
        latexSource = latexSource.replace(placeholder.toString(), value);
    }

    public String compile() throws IOException, InterruptedException {
        if (latexSource.isEmpty()) {
            throw new RuntimeException("latexSource is empty. Fill it before calling compile().");
        }

        String latexFilePath = "temp.tex";
        String pdfFilePath = "output.pdf";

        // Write LaTeX source to a temporary file
        java.nio.file.Files.write(java.nio.file.Paths.get(latexFilePath), latexSource.getBytes());

        // Compile LaTeX to PDF using pdflatex
        ProcessBuilder processBuilder = new ProcessBuilder("pdflatex", "-output-directory=.", latexFilePath);
        Process process = processBuilder.start();
        if (process.waitFor() != 0) {
            throw new RuntimeException("pdflatex exited with non-zero code.");
        }

        // Clean up temporary file
        if (!new File(latexFilePath).delete()) {
            System.err.println(latexFilePath + " was not deleted");
        }

        return pdfFilePath;
    }

    /*public static void main(String[] args) {
        // LaTeX source
        String latexSource = """
                \\documentclass[12pt]{article}
                               
                               % Set the default font
                               \\usepackage{tgpagella}
                               
                               \\usepackage[utf8]{inputenc} % For input encoding
                               \\usepackage{geometry}
                               \\geometry{a4paper, margin=0.75in}
                               \\usepackage{titlesec}
                               \\usepackage{hyperref}
                               \\hypersetup{
                                   colorlinks=true,
                                   linkcolor=black,
                                   urlcolor=black,
                               }
                               
                               \\titleformat{\\section}{\\fontfamily{phv}\\large\\bfseries}{}{1em}{}[\\titlerule] % Section title format
                               
                               % empty the headers, footers, pagenumbers…
                               \\pagestyle{empty}
                               
                               \\begin{document}
                               \\begin{center}
                                   \\fontfamily{phv}\\Huge\\textbf{Your Name} \\\\
                                   \\fontfamily{phv}\\large\\textbf{Your Position}
                               \\end{center}
                               Saint Petersburg \\hfill +7952812\\\\
                               Russia \\hfill \\href{mailto:example@gmail.com}{example@gmail.com} \\\\\s
                               
                               \\section{Summary}
                               Your summary
                               
                               
                               \\section{Education}
                               \\begin{tabular}{ l p{15in} }
                                   2022 - 2024 & \\textbf{Course name} \\newline School name, school address\s
                                   \\newline Awaiting, \\textbf{\\textit{Grade CGPA}} \\\\\s
                               
                                   2019 - 2022 & \\textbf{Course name} \\newline School name, school address\s
                                   \\newline Awaiting, \\textbf{\\textit{Grade CGPA}} \\\\\s
                               \\end{tabular}
                               
                               \\section{Experience}
                               \\begin{tabular}{ l p{15in} }
                                   2022 - 2024 & \\textbf{Company name} \\newline tasks
                                   \\newline Awaiting, \\textbf{\\textit{Grade CGPA}} \\\\
                               
                               \\end{tabular}
                               
                               \\section{Skills}
                               \\begin{itemize}
                                   \\itemsep=-.3em
                                   \\item \\textbf{Programming:} programming Knowledge you have
                                   \\item \\textbf{Tools:} Additional Tools knowledge
                                   \\item \\textbf{Miscellaneous:} additional skills
                               \\end{itemize}
                               
                               \\section{Hobbies}
                               Your Hobbies goes here.
                               
                               \\end{document}""";

        // Temporary files
        String latexFilePath = "temp.tex";
        String pdfFilePath = "output.pdf";

        // Write LaTeX source to a temporary file
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(latexFilePath), latexSource.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Compile LaTeX to PDF using pdflatex
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("pdflatex", "-output-directory=.", latexFilePath);
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        // Clean up temporary files (optional)
//        new File(latexFilePath).delete();

        System.out.println("PDF generated at: " + pdfFilePath);
    }*/
}
