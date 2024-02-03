package com.softedge.solution.commons;

import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;
import de.l3s.boilerpipe.sax.HTMLDocument;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

/**
 * Created by User on 25/11/2016.
 */
@Service
public class CommonUtilities {

    public static DateFormat IN_TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Get only the domain name.
     * This removes prefixes like http:// and https:// and special characters and www.
     * and removes any thing after the domain name
     * Example: Inputs & Outputs
     * http://www.altiux.com  -->  altiux.com
     * https://www.altiux.com -->  altiux.com
     * ....abcd.com           -->  abcd.com
     * -abcd.com              -->  abcd.com
     * .advshutter.co.uk      -->  advshutter.co.uk
     * apparent-energy.com    -->  apparent-energy.com
     * bespoke-digital.co.uk  -->  bespoke-digital.co.uk
     * www.advshutter.co.uk/abouts.html --> advshutter.co.uk
     */
    public static String getDomainNameOfURL(String url) {
        if (null == url) {
            return null;
        } else {
            url = url.trim();
        }
        String DOMAIN_NAME_PATTERN = "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
        Pattern patternDomainName = Pattern.compile(DOMAIN_NAME_PATTERN);
        Matcher matcher = patternDomainName.matcher(url);
        String domainName = "";
        if (matcher.find()) {
            domainName = matcher.group(0).toLowerCase().trim();
        } else {
            //if it didn't find any match (i.e. if there is no http:// or https:// found in the url return same text back
            // like if the input is www.altiux.com or altiux.com or altiux -- return the same input parameter.
            domainName = url;
        }

        if (null != domainName) {
            if (domainName.startsWith("www.")) {
                domainName = domainName.substring(4);
            } else {
                String SPECIAL_CHARACTERS_PATTERN = "^[^A-Za-z0-9]*";
                Pattern pattern = Pattern.compile(SPECIAL_CHARACTERS_PATTERN);
                Matcher matcherDomain = pattern.matcher(domainName);
                if (matcherDomain.find()) {
                    domainName = domainName.substring(matcherDomain.group(0).length(), domainName.length());
                }
            }
            domainName = domainName.trim();
        }
        return domainName;
    }

    /*****************************************************
     * input: company url will be "http://www.altiux.com" OR https://altiux.com OR altiux.com
     * output: domain name will be "altiux.com"
     *****************************************************//*
    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        if (null != uri.getHost()) {
            url = uri.getHost();
        }
        if (null != url) {
            if (url.startsWith("www.")) {
                url = url.substring(4);
            } else {
                String SPECIAL_CHARACTERS_PATTERN = "^[^A-Za-z0-9]*";
                Pattern pattern = Pattern.compile(SPECIAL_CHARACTERS_PATTERN);
                Matcher matcherDomain = pattern.matcher(url);
                if (matcherDomain.find()) {
                    url = url.substring(matcherDomain.group(0).length(), url.length());
                }
            }
        }
        return url;
    }*/

    /**
     * Create a byte array with a specific size filled with specified data.
     *
     * @param size the size of the byte array
     * @param data the data to put in the byte array
     * @return the JSON byte array
     */
    public static byte[] createByteArray(int size, String data) {
        byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++) {
            byteArray[i] = Byte.parseByte(data, 2);
        }
        return byteArray;
    }

    /**
     * Convert byteArray to String
     *
     * @param byteArray
     * @return
     */
    public static String convertByteArrayToString(byte[] byteArray) {
        if (null == byteArray || byteArray.length == 0) return null;
        return new String(byteArray);
    }

    /**
     * Convert String to byteArray
     *
     * @param data
     * @return
     */
    public static byte[] convertStringToByteArray(String data) {
        if (null == data || data.trim().isEmpty()) return null;
        return data.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] convertStringToCompressedByteArray(String stringData) throws IOException {
        if (null == stringData || stringData.trim().isEmpty()) return null;
        /*byte[] data = stringData.getBytes(StandardCharsets.UTF_8);

        Deflater deflater = new Deflater();
        deflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        System.out.println("Original: " + data.length / 1024 + " Kb");
        System.out.println("Compressed: " + output.length / 1024 + " Kb");
        return output;*/

        byte[] decodedFromBase64Format = Base64.decodeBase64(stringData.getBytes(StandardCharsets.UTF_8));
        return decodedFromBase64Format;
    }

    public static String decompressCompressedByteArrayToString(byte[] data) throws IOException, DataFormatException {
        /*Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        System.out.println("Original: " + data.length);
        System.out.println("Compressed: " + output.length);

        if(null == output || output.length == 0) return null;
        return new String(output);*/

        byte[] byteArrayInBase64Format = Base64.encodeBase64(data);
        return new String(byteArrayInBase64Format);
    }

    public static byte[] convertBase64EncodedImageToImageFile(String base64EncodedString) {
        Base64 decoder = new Base64();
        byte[] imgBytes = decoder.decode(base64EncodedString);
        return imgBytes;
    }

    public static String convertLocalDateToString(LocalDate localDate, String dateFormat) {
        if (localDate == null || dateFormat == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return formatter.format(localDate);
    }

    public static LocalDate convertStringToLocalDate(String dateStr, String dateFormat) {
        if (dateStr == null || dateFormat == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDate localDate = LocalDate.parse(dateStr, formatter);
        return localDate;
    }


    public String getKeyFromValue(Map hm, String value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return (String) o;
            }
        }
        return null;
    }


    /*****************************************************
     * input: company url will be "http://www.altiux.com"
     * output: domain name will be "altiux.com"
     *****************************************************/
    public static String getCompanyDomainName(String url) {
        String companyDomain = getDomainNameOfURL(url);
        String trimmedCompanyDomain = companyDomain;

        if (trimmedCompanyDomain != null && !trimmedCompanyDomain.isEmpty()) {
            String REGEX = "www.|http://|https://";
            String REPLACE = "";
            Pattern p = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(trimmedCompanyDomain);
            trimmedCompanyDomain = m.replaceAll(REPLACE);
        }
        return trimmedCompanyDomain;
    }

    /*****************************************************
     * input: domain name will be "www.altiux.com"
     * output: trimmed name will be "altiux"
     *****************************************************/
    public static String getTrimmedDomainName(String url) {
        String companyDomain = getDomainNameOfURL(url);
        String trimmedCompanyDomain = companyDomain;

        if (trimmedCompanyDomain != null && !trimmedCompanyDomain.isEmpty()) {
            String REGEX = "www.|http://|https://|.com|.co|.in";
            String REPLACE = "";
            Pattern p = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(trimmedCompanyDomain);
            trimmedCompanyDomain = m.replaceAll(REPLACE);

            int firstIndex = trimmedCompanyDomain.indexOf(".");
            if (firstIndex >= 0) {
                trimmedCompanyDomain = trimmedCompanyDomain.substring(0, trimmedCompanyDomain.indexOf("."));
            }
        }
        return trimmedCompanyDomain;
    }


    public static String getCountryName(String countryCode) {

        String countryName = "";
        if (countryCode != null && !countryCode.isEmpty()) {
            Locale locale = new Locale("", countryCode);
            countryName = locale.getDisplayName();
        } else {
            System.out.println("Invalid Country Code is given !!!");
            return "";
        }

        return countryName;
    }


    public static Integer getYearFromString(String strDate) {
        if ("null".equalsIgnoreCase(strDate) || strDate.isEmpty()) {
            return null;
        }

        org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
                .withLocale(Locale.UK);
        Integer year = null;
        if (!StringUtils.equals(strDate, "0000-00-00")) {
            org.joda.time.LocalDate date = formatter.parseLocalDate(strDate);
            year = date.getYear();
        }
        return year;
    }

    //Linux set File permission 664
    public static void setPermission(File file) throws IOException {
        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);

        perms.add(PosixFilePermission.OTHERS_READ);

        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);

        Files.setPosixFilePermissions(file.toPath(), perms);
    }

    public static String insertPeriodically(
            String text, String insert, int period) {
        StringBuilder builder = new StringBuilder(
                text.length() + insert.length() * (text.length() / period) + 1);

        int index = 0;
        String prefix = "";
        while (index < text.length()) {
            // Don't put the insert in the very first iteration.
            // This is easier than appending it *after* each substring
            builder.append(prefix);
            prefix = insert;
            builder.append(text.substring(index,
                    Math.min(index + period, text.length())));
            index += period;
        }
        return builder.toString();
    }

    public static Map<String, Long> sortByValue(Map<String, Long> unsortMap) {

        List<Map.Entry<String, Long>> list =
                new LinkedList<Map.Entry<String, Long>>(unsortMap.entrySet());


        Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1,
                               Map.Entry<String, Long> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<String, Long> sortedMap = new LinkedHashMap<String, Long>();
        for (Map.Entry<String, Long> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;


    }

    public static BigDecimal getBigDecimal(Object value) {
        BigDecimal result = new BigDecimal(0);
        if (value != null) {
            try {
                if (value instanceof BigDecimal) {
                    result = (BigDecimal) value;
                } else if (value instanceof String) {
                    result = new BigDecimal((String) value);
                } else if (value instanceof BigInteger) {
                    result = new BigDecimal((BigInteger) value);
                } else if (value instanceof Number) {
                    result = new BigDecimal(((Number) value).doubleValue());
                } else {
                    //throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
                    System.out.println("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal. " + e);
            } catch (ClassCastException e) {
                System.out.println("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal. " + e);
            } catch (Exception e) {
                System.out.println("Exception caught. " + e);
            }
        }
        return result;
    }

    /*****************************************************
     * input: comma separated values as a string
     * output: array of values
     *****************************************************/
    public static String[] getStringArrayFromCommaSeparatedString(String commaSeparatedString) {
        String[] ccList = null;
        if (null != commaSeparatedString && !commaSeparatedString.trim().isEmpty()) {
            ccList = commaSeparatedString.split(",");
        }
        return ccList;
    }

    public static String getHtmlTextContent(byte[] dataByte) throws Exception {
        final HTMLDocument htmlDoc = new HTMLDocument(dataByte, Charset.forName("UTF-8"));
        final TextDocument doc = new BoilerpipeSAXInput(htmlDoc.toInputSource()).getTextDocument();
        String content = CommonExtractors.ARTICLE_EXTRACTOR.getText(doc);
        return content;
    }

/*    public static Optional<String> extractImageUrl(String url) throws IOException {
        Optional<String> mainImage = MainImageResolver.resolveMainImage(url);
        return mainImage;
    }*/

    public static InputStream resizeImage(InputStream inputStream, int width, int height) throws IOException {
        BufferedImage sourceImage = ImageIO.read(inputStream);
        Image thumbnail = null;
        BufferedImage bufferedThumbnail = null;
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream outInputStream = null;
        try {
            if (null != sourceImage) {
                thumbnail = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                if (thumbnail != null) {
                    bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null),
                            thumbnail.getHeight(null),
                            BufferedImage.TYPE_INT_RGB);
                    bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);
                    baos = new ByteArrayOutputStream();
                    ImageIO.write(bufferedThumbnail, "jpeg", baos);
                    outInputStream = new ByteArrayInputStream(baos.toByteArray());
                }
            }
            return outInputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return outInputStream;
        }

    }

    public static boolean isEmpty(String string) {
        if (string == null || string.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String string) {
        if (string == null || string.trim().length() == 0) {
            return false;
        }
        return true;
    }

    public static boolean isNotEmpty(Object obj) {
        if (obj == null) {
            return false;
        }
        return true;
    }


    /**
     * Convert an Object to a Timestamp, without an Exception
     */
    public static java.sql.Timestamp getTimestamp(Object value) {
        try {
            return toTimestamp(value);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }

    /**
     * Convert an Object to a Timestamp.
     */
    public static java.sql.Timestamp toTimestamp(Object value) throws ParseException {
        if (value == null) return null;
        if (value instanceof java.sql.Timestamp) return (java.sql.Timestamp) value;
        if (value instanceof String) {
            if ("".equals((String) value)) return null;
            return new java.sql.Timestamp(IN_TIMESTAMP_FORMAT.parse((String) value).getTime());
        }

        return new java.sql.Timestamp(IN_TIMESTAMP_FORMAT.parse(value.toString()).getTime());
    }

    public enum OPERATION {
        ADD,
        UPDATE,
        DELETE,
        VIEW
    }

    /**
     * Convert Date to last min of the day.
     *
     * @return
     */
    public static ZonedDateTime convertDateToEndOfDay(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .atStartOfDay(ZoneOffset.systemDefault()).withHour(23).withMinute(59);
    }

    /**
     * Check whether its a valid domain name or not
     */
    public static boolean isValidDomain(String str) {
        // Regex to check valid domain name.
        String regex = "^((?!-)[A-Za-z0-9-]"
                + "{1,63}(?<!-)\\.)"
                + "+[A-Za-z]{2,6}";

        // Compile the ReGex
        Pattern p
                = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher()
        // method to find the matching
        // between the given string and
        // regular expression.
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

    public static int random6DigitNumberGenerator() {
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }

    public static int random4DigitNumberGenerator() {
        return ThreadLocalRandom.current().nextInt(1000, 10000);
    }

    public static boolean isNullOrEmpty(Object obj) {
        if (obj instanceof String) {
            String str = obj.toString();
            if (str != null && !str.trim().isEmpty())
                return false;
            return true;
        } else {
            if (obj != null)
                return false;
            return true;
        }
    }

    public static List<String> getListSplitByComma(String elements) {
        List<String> listOfElements = new ArrayList<>();
        if (elements != null) {
            String[] arrayOfElements = elements.split(",");
            for (String ele : arrayOfElements) {
                if (!CommonUtilities.isNullOrEmpty(ele)) {
                    listOfElements.add(ele.trim());
                }
            }
            return listOfElements;
        } else {
            return new ArrayList<>();
        }
    }

    public static String getStringSeperatedCommaFromList(List<? extends Object> listOfElements) {
        String allElements = null;
        if (listOfElements != null) {
            for (Object element : listOfElements) {
                if (!CommonUtilities.isNullOrEmpty(element)) {
                    if (allElements == null) {
                        allElements = element.toString();
                    } else {
                        allElements = allElements + "," + element;
                    }
                }
            }
            return allElements;
        } else {
            return null;
        }
    }

    public static String getStringSeperatedCommaNdQuotesFromList(List<? extends Object> listOfElements) {
        String allElements = null;
        if (listOfElements != null) {
            for (Object element : listOfElements) {
                if (!CommonUtilities.isNullOrEmpty(element)) {
                    if (allElements == null) {
                        allElements = "\"" + element.toString() + "\"";
                    } else {
                        allElements = allElements + ",\"" + element + "\"";
                    }
                }
            }
            return allElements;
        } else {
            return null;
        }
    }


    public static <T, U> List<U> convertStringListToIntList(List<T> listOfString, Function<T, U> function) {
        return listOfString.stream()
                .map(function)
                .collect(Collectors.toList());
    }

    public static <T, U> List<U> convertStringSplitRegexToIntList(String value, String regex) {
        return (List<U>) Arrays.stream(value.split(regex))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public static String[] mergeStringArray(String[] array1, String[] array2) {
        int aLen = array1.length;
        int bLen = array2.length;
        String[] result = new String[aLen + bLen];
        System.arraycopy(array1, 0, result, 0, aLen);
        System.arraycopy(array2, 0, result, aLen, bLen);
        return result;
    }

    /**
     * @param data function used to convert the Camel Case string data into normal readable format
     * @return Srting type parsed data from camel case to Human readbale format
     */

    public static String splitCamelCase(String data) {
        return data.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    /**
     * Used to convert the list of String items to Array of String
     *
     * @param headers passing the Parameter which needs to be typecasted to String Array
     */
    public static String[] getStringArray(List<String> headers) {
        String[] str = new String[headers.size()];
        Object[] objArr = headers.toArray();
        int i = 0;
        for (Object obj : objArr) {
            str[i++] = (String) obj;
        }
        return str;
    }


}
