package br.com.brasilprev.utils;

import io.restassured.path.json.JsonPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class DataPool {

    private String fileName;
    private static File[] matchingFiles = null;
    private static String filePath = null;

    public DataPool() {

    }

    public DataPool(String fileName) {
        this.fileName = fileName;
    }

    public static JsonPath dataPool(String fileName) throws FileNotFoundException {
        return JsonPath.from(getFileFromPath(fileName));
    }

    public String pegarUmCampoEmEspecificoNaResponse(String list, String key, String value, String field) throws FileNotFoundException {

        return (String) filterData(list, key, value)
                .findFirst()
                .get()
                .get(field);
    }

    public Map pegarUmObjetoJSONDeUmaListaNoResponse(String list, String key, String value) throws FileNotFoundException {

        return filterData(list, key, value)
                .findAny()
                .get();
    }

    private Stream<Map> filterData(String list, String key, String value) throws FileNotFoundException {

        JsonPath jsonPath = JsonPath.from(getFileFromPath(this.fileName));
        List<Map> array = jsonPath.get(list);
        return array
                .stream()
                .filter(map -> map.containsKey(key)
                        && map.containsValue(value));
    }

    private static String listar(File directory, final String startsWith, final String endsWith) {

        if (directory.isDirectory()) {
            String[] subDirectory = directory.list();
            if (subDirectory != null) {
                for (String dir : directory.list()) {
                    if (dir.startsWith(startsWith) && dir.endsWith(endsWith) && !directory.getPath().contains("bin")) {
                        matchingFiles = directory.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return name.startsWith(startsWith) && name.endsWith(endsWith);
                            }
                        });
                        if (matchingFiles.length > 0) {
                            filePath = matchingFiles[0].getAbsolutePath().toString();
                            //base.Log.info(matchingFiles[0].toString());
                        }
                    }
                    listar(new File(directory + File.separator + dir), startsWith, endsWith);

                }
            }
        }
        return filePath;
    }

    public String getPathArquivo(String file) throws FileNotFoundException {
        String[] fileWithExtension = file.split("\\.");
        String filePath = listar(new File("src" + File.separator), fileWithExtension[0], fileWithExtension[1]);
        if (filePath == null)
            throw new FileNotFoundException("Arquivo não encontrado: " + file);
        return filePath;
    }

    public static File getFileFromPath(String file) throws FileNotFoundException {
        String[] fileWithExtension = file.split("\\.");
        String filePath = listar(new File("src" + File.separator), fileWithExtension[0], fileWithExtension[1]);
        if (filePath == null)
            throw new FileNotFoundException("Arquivo não encontrado: " + file);
        return new File(filePath);
    }
}
