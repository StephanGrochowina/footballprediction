package de.coiaf.footballprediction.backend.importresults.singleseason;

import de.coiaf.footballprediction.backend.importresults.common.BuilderMatchResult;
import de.coiaf.footballprediction.backend.importresults.entity.MatchResult;
import de.coiaf.footballprediction.backend.model.sharedcontext.ThresholdTotalGoals;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import javax.ejb.Stateless;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Importer for football data per single season from http://www.football-data.co.uk
 * Available data fields are described at http://www.football-data.co.uk/notes.txt
 */
@Stateless
public class ImporterFootballDataCoUkFilePerSeason extends AbstractImporterSeason {

    private enum BookmakersMatchOutcome {
        B365("Bet365", "B365H", "B365D", "B365A"),
        BS("Blue Square", "BSH", "BSD", "BSA"),
        BW("Bet&Win", "BWH", "BWD", "BWA"),
        GB("Gamebookers", "GBH", "GBD", "GBA"),
        IW("Interwetten", "IWH", "IWD", "IWA"),
        LB("Ladbrokes", "LBH", "LBD", "LBA"),
        PS("Pinnacle", "PSH", "PSD", "PSA"),
        P("Pinnacle", "PH", "PD", "PA"),
        SO("Sporting Odds", "SOH", "SOD", "SOA"),
        SB("Sportingbet", "SBH", "SBD", "SBA"),
        SJ("Stan James", "SJH", "SJD", "SJA"),
        SY("Stanleybet", "SYH", "SYD", "SYA"),
        VC("VC Bet", "VCH", "VCD", "VCA"),
        WH("William Hill", "WHH", "WHD", "WHA"),
        BbMx("Betbrain (maximum)", "BbMxH", "BbMxD", "BbMxA"),
        BbAv("Betbrain (average)", "BbAvH", "BbAvD", "BbAvA"),
        Max("Oddsportal (maximum)", "MaxH", "MaxD", "MaxA"),
        Avg("Oddsportal (average)", "AvgH", "AvgD", "AvgA"),
        PSC("Pinnacle closing odds", "PSCH", "PSCD", "PSCA");

        public static List<String> getHeaders() {
            List<String> headers = new ArrayList<>();

            for (BookmakersMatchOutcome bookmaker : BookmakersMatchOutcome.values()) {
                if (bookmaker.columnOddHomeWin != null) {
                    headers.add(bookmaker.columnOddHomeWin);
                }
                if (bookmaker.columnOddDraw != null) {
                    headers.add(bookmaker.columnOddDraw);
                }
                if (bookmaker.columnOddAwayWin != null) {
                    headers.add(bookmaker.columnOddAwayWin);
                }
            }

            return headers;
        }

        private final String bookmakerName;
        private final String columnOddHomeWin;
        private final String columnOddDraw;
        private final String columnOddAwayWin;

        BookmakersMatchOutcome(String bookmakerName, String columnOddHomeWin, String columnOddDraw, String columnOddAwayWin) {
            this.bookmakerName = bookmakerName;
            this.columnOddHomeWin = columnOddHomeWin;
            this.columnOddDraw = columnOddDraw;
            this.columnOddAwayWin = columnOddAwayWin;
        }

        public String getBookmakerName() {
            return this.bookmakerName;
        }

        public String getColumnOddHomeWin() {
            return this.columnOddHomeWin;
        }

        public String getColumnOddDraw() {
            return this.columnOddDraw;
        }

        public String getColumnOddAwayWin() {
            return this.columnOddAwayWin;
        }
    }

    private enum BookmakersMatchTotalGoals {
        B365("Bet365", DEFAULT_THRESHOLD, "B365<2.5", "B365>2.5"),
        GB("Gamebookers", DEFAULT_THRESHOLD, "GB<2.5", "GB>2.5"),
        BbMx("Betbrain (maximum)", DEFAULT_THRESHOLD, "BbMx<2.5", "BbMx>2.5"),
        BbAv("Betbrain (average)", DEFAULT_THRESHOLD, "BbAv<2.5", "BbAv>2.5");

        public static List<String> getHeaders() {
            List<String> headers = new ArrayList<>();

            for (BookmakersMatchTotalGoals bookmaker : BookmakersMatchTotalGoals.values()) {
                if (bookmaker.columnOddLessGoalsThanThreshold != null) {
                    headers.add(bookmaker.columnOddLessGoalsThanThreshold);
                }
                if (bookmaker.columnOddMoreGoalsThanThreshold != null) {
                    headers.add(bookmaker.columnOddMoreGoalsThanThreshold);
                }
            }

            return headers;
        }

        private final String bookmakerName;
        private final ThresholdTotalGoals threshold;
        private final String columnOddLessGoalsThanThreshold;
        private final String columnOddMoreGoalsThanThreshold;

        BookmakersMatchTotalGoals(String bookmakerName, ThresholdTotalGoals threshold, String columnOddLessGoalsThanThreshold, String columnOddMoreGoalsThanThreshold) {
            this.bookmakerName = bookmakerName;
            this.threshold = threshold;
            this.columnOddLessGoalsThanThreshold = columnOddLessGoalsThanThreshold;
            this.columnOddMoreGoalsThanThreshold = columnOddMoreGoalsThanThreshold;
        }

        public String getBookmakerName() {
            return this.bookmakerName;
        }

        public ThresholdTotalGoals getThreshold() {
            return this.threshold;
        }

        public String getColumnOddLessGoalsThanThreshold() {
            return this.columnOddLessGoalsThanThreshold;
        }

        public String getColumnOddMoreGoalsThanThreshold() {
            return this.columnOddMoreGoalsThanThreshold;
        }

    }

    private enum ColumnsMatchData {
//        COLUMN_DIVISION("Div"),
        COLUMN_MATCH_DAY("Date"),
        COLUMN_HOME_TEAM("HomeTeam"),
        COLUMN_AWAY_TEAM("AwayTeam"),
        COLUMN_FULL_TIME_HOME_GOALS_V1("FTHG"),
        COLUMN_FULL_TIME_HOME_GOALS_V2("HG"),
        COLUMN_FULL_TIME_AWAY_GOALS_V1("FTAG"),
        COLUMN_FULL_TIME_AWAY_GOALS_V2("AG"),
//        COLUMN_FULL_TIME_RESULT_V1("FTR"),
//        COLUMN_FULL_TIME_RESULT_V2("Res"),
        COLUMN_HALF_TIME_HOME_GOALS("HTHG"),
        COLUMN_HALF_TIME_AWAY_GOALS("HTAG"),
//        COLUMN_HALF_TIME_RESULT("HTR"),
//        COLUMN_ATTENDANCE("Attendance"),
//        COLUMN_REFEREE_NAME("Referee"),
        COLUMN_HOME_TEAM_SHOTS("HS"),
        COLUMN_AWAY_TEAM_SHOTS("AS"),
        COLUMN_HOME_TEAM_SHOTS_ON_TARGET("HST"),
        COLUMN_AWAY_TEAM_SHOTS_ON_TARGET("AST"),
//        COLUMN_HOME_TEAM_HIT_WOODWORK("HHW"),
//        COLUMN_AWAY_TEAM_HIT_WOODWORK("AHW"),
        COLUMN_HOME_TEAM_CORNERS("HC"),
        COLUMN_AWAY_TEAM_CORNERS("AC"),
        COLUMN_HOME_TEAM_FOULS_COMMITTED("HF"),
        COLUMN_AWAY_TEAM_FOULS_COMMITTED("AF"),
        COLUMN_HOME_TEAM_FREE_KICKS_CONCEDED("HFKC"),
        COLUMN_AWAY_TEAM_FREE_KICKS_CONCEDED("AFKC"),
        COLUMN_HOME_TEAM_OFFSIDES("HO"),
        COLUMN_AWAY_TEAM_OFFSIDES("AO"),
        COLUMN_HOME_TEAM_YELLOW_CARDS("HY"),
        COLUMN_AWAY_TEAM_YELLOW_CARDS("AY"),
        COLUMN_HOME_TEAM_RED_CARDS("HR"),
        COLUMN_AWAY_TEAM_RED_CARDS("AR"),
//        COLUMN_HOME_TEAM_BOOKING_POINTS("HBP"),
//        COLUMN_AWAY_TEAM_BOOKING_POINTS("ABP"),
        ;

        public static List<String> getHeaders() {
            List<String> headers = new ArrayList<>();

            for (ColumnsMatchData column : ColumnsMatchData.values()) {
                if (column.columName != null) {
                    headers.add(column.columName);
                }
            }

            return headers;
        }

        private final String columName;

        ColumnsMatchData(String columName) {
            this.columName = columName;
        }

        public String getColumName() {
            return this.columName;
        }
    }

    private static final ThresholdTotalGoals DEFAULT_THRESHOLD = ThresholdTotalGoals.getDefaultInstance();
    private static final Charset SOURCE_CHARSET = Charset.forName("ISO-8859-1");

    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("dd/MM/yy");
    private static final DateTimeFormatter FORMATTER_DATE_LONG = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Pattern PATTERN_DATE = Pattern.compile("\\d{2}/\\d{2}/\\d{2}");
    private static final Pattern PATTERN_DATE_LONG = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");

    private static final Supplier<String[]> SUPPLIER_COLUMN_HEADERS = () -> {
        List<String> headers = new ArrayList<>();

        headers.addAll(ColumnsMatchData.getHeaders());
        headers.addAll(BookmakersMatchOutcome.getHeaders());
        headers.addAll(BookmakersMatchTotalGoals.getHeaders());

        //noinspection ToArrayCallWithZeroLengthArrayArgument
        return headers.toArray(new String[headers.size()]);
    };
    private static final String[] COLUMN_HEADERS = SUPPLIER_COLUMN_HEADERS.get();
    private static final CSVFormat SOURCE_IMPORT_FORMAT = CSVFormat.RFC4180.withHeader(COLUMN_HEADERS)
            .withIgnoreHeaderCase()
            .withTrim()
            .withIgnoreEmptyLines();

    @Override
    protected CSVFormat getSourceImportFormat() {
        return SOURCE_IMPORT_FORMAT;
    }

    private LocalDate extractDate(String rawValue) {
        LocalDate result = null;

        if (rawValue != null) {
            if (PATTERN_DATE.matcher(rawValue).matches()) {
                result = LocalDate.parse(rawValue, FORMATTER_DATE);

                if (LocalDate.now().isBefore(result)) {
                    result = result.minusYears(100);
                }
            }
            else if (PATTERN_DATE_LONG.matcher(rawValue).matches()) {
                result = LocalDate.parse(rawValue, FORMATTER_DATE_LONG);
            }
        }

        return result;
    }

    @Override
    protected void parseRecord(ContextResultImport context, CSVRecord record) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(record);

        MatchResult match;

        BuilderMatchResult builder =  BuilderMatchResult
                .createInstance(context.getContextSeasonImport(),
                        () -> this.getImporterCsv().getString(record, ColumnsMatchData.COLUMN_HOME_TEAM.getColumName()),
                        () -> this.getImporterCsv().getString(record, ColumnsMatchData.COLUMN_AWAY_TEAM.getColumName()),
                        () -> this.getImporterCsv().getLocalDate(record, ColumnsMatchData.COLUMN_MATCH_DAY.getColumName(), this::extractDate))
                .addScoreHalfTime(
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_HALF_TIME_HOME_GOALS.getColumName()),
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_HALF_TIME_AWAY_GOALS.getColumName()))
                .addScoreFullTime(
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_FULL_TIME_HOME_GOALS_V1.getColumName()),
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_FULL_TIME_AWAY_GOALS_V1.getColumName()))
                .addScoreFullTime(
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_FULL_TIME_HOME_GOALS_V2.getColumName()),
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_FULL_TIME_AWAY_GOALS_V2.getColumName()))
                .addMatchStatShots(
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_HOME_TEAM_SHOTS.getColumName()),
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_AWAY_TEAM_SHOTS.getColumName()))
                .addMatchStatShotsOnTarget(
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_HOME_TEAM_SHOTS_ON_TARGET.getColumName()),
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_AWAY_TEAM_SHOTS_ON_TARGET.getColumName()))
                .addMatchStatCorners(
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_HOME_TEAM_CORNERS.getColumName()),
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_AWAY_TEAM_CORNERS.getColumName()))
                .addMatchStatFoulsCommitted(
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_HOME_TEAM_FOULS_COMMITTED.getColumName()),
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_AWAY_TEAM_FOULS_COMMITTED.getColumName()))
                .addMatchStatFreeKicksConceded(
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_HOME_TEAM_FREE_KICKS_CONCEDED.getColumName()),
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_AWAY_TEAM_FREE_KICKS_CONCEDED.getColumName()))
                .addMatchStatOffsides(
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_HOME_TEAM_OFFSIDES.getColumName()),
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_AWAY_TEAM_OFFSIDES.getColumName()))
                .addMatchStatYellowCards(
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_HOME_TEAM_YELLOW_CARDS.getColumName()),
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_AWAY_TEAM_YELLOW_CARDS.getColumName()))
                .addMatchStatRedCards(
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_HOME_TEAM_RED_CARDS.getColumName()),
                        () -> this.getImporterCsv().getInteger(record, ColumnsMatchData.COLUMN_AWAY_TEAM_RED_CARDS.getColumName()))
                ;

        for (BookmakersMatchOutcome bookmakerMatchOutcome : BookmakersMatchOutcome.values()) {
            builder.addOddsMatchOutcome(
                    bookmakerMatchOutcome::getBookmakerName,
                    () -> this.getImporterCsv().getBigDecimal(record, bookmakerMatchOutcome.getColumnOddHomeWin()),
                    () -> this.getImporterCsv().getBigDecimal(record, bookmakerMatchOutcome.getColumnOddDraw()),
                    () -> this.getImporterCsv().getBigDecimal(record, bookmakerMatchOutcome.getColumnOddAwayWin()));
        }

        for (BookmakersMatchTotalGoals bookmakerMatchTotalGoals : BookmakersMatchTotalGoals.values()) {
            builder.addOddsMatchTotalGoals(
                    bookmakerMatchTotalGoals::getBookmakerName,
                    () -> DEFAULT_THRESHOLD,
                    () -> this.getImporterCsv().getBigDecimal(record, bookmakerMatchTotalGoals.getColumnOddLessGoalsThanThreshold()),
                    () -> this.getImporterCsv().getBigDecimal(record, bookmakerMatchTotalGoals.getColumnOddMoreGoalsThanThreshold()));
        }

        match =  builder.build();

        this.persistMatch(match);
    }

    @Override
    protected Charset getSourceCharset() {
        return SOURCE_CHARSET;
    }
}
