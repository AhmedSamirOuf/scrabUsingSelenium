import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String [] args) throws IOException {
        ChromeDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        System.setProperty("WebDriver.Chrome.driver", "../chromedriver");
        navigateToIMDB(driver);
        sortMoviesInAscendingOrder(driver);
        List<WebElement> MoviesTitlesWebElements = getMoviesWebElements(driver, wait);
        List<String> MoviesTitles = convertMovieTitleWebElementToStrings(MoviesTitlesWebElements);
        driver.quit();

        String[] columns = {"rank","movie","year"};
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("movies");
        Row headerRow = sheet.createRow(0);
        setColumns(columns, headerRow);
        int rowNum = 1;
        fillExcelSheet(MoviesTitles, sheet, rowNum);
        resizeColumns(columns, sheet);
        FileOutputStream fileOut = saveExcelSheet(workbook);
        fileOut.close();
        workbook.close();
    }

    private static void resizeColumns(String[] columns, Sheet sheet) {
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private static FileOutputStream saveExcelSheet(Workbook workbook) throws IOException {
        FileOutputStream fileOut = new FileOutputStream("top 50 movies ascending.xlsx");
        workbook.write(fileOut);
        return fileOut;
    }

    private static void setColumns(String[] columns, Row headerRow) {
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
    }

    private static void fillExcelSheet(List<String> moviesTitles, Sheet sheet, int rowNum) {
        for (String moviesTitle : moviesTitles) {
            Row row = sheet.createRow(rowNum++);
            String regexSplit = "(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)";
            String[] movieDetails = moviesTitle.split(regexSplit);
            String movieRank = movieDetails[0];
            row.createCell(0)
                    .setCellValue(movieRank);

            String movieName = movieDetails[1].substring(2, movieDetails[1].length() - 2);
            row.createCell(1)
                    .setCellValue(movieName);

            String movieYear = movieDetails[2];
            row.createCell(2)
                    .setCellValue(movieYear);
        }
    }


    private static List<String> convertMovieTitleWebElementToStrings(List<WebElement> moviesTitlesWebElements) {
        List<String> MoviesTitles = new ArrayList<String>();
        for(WebElement element : moviesTitlesWebElements){
            MoviesTitles.add(element.getText());
        }
        return MoviesTitles;
    }

    private static List<WebElement> getMoviesWebElements(ChromeDriver driver, WebDriverWait wait) {
        String first50MovieXPath = "//*[@id=\"main\"]/div/div[3]/div";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(first50MovieXPath)));

        WebElement first50MovieElement = driver.findElement(By.xpath(first50MovieXPath));
        return first50MovieElement.findElements(By.className("lister-item-header"));
    }

    private static void navigateToIMDB(ChromeDriver driver) {
        String url = "https://www.imdb.com/search/title/?groups=top_250&sort=user_rating,desc";
        driver.navigate().to(url);
    }

    private static void sortMoviesInAscendingOrder(ChromeDriver driver) {
        String userRatingAscendingFilter = "//*[@id=\"main\"]/div/div[2]/a[3]";
        driver.findElement(By.xpath(userRatingAscendingFilter)).click();
    }
}
