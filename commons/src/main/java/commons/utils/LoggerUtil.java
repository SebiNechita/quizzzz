package commons.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.*;
import java.util.regex.Pattern;

/**
 * Library which integrates with java.util.Logging and extends it for easy use.
 * This plugin was made for personal use. Bugs/inconsistencies may occur.
 *
 * @author Sander Vermeulen
 */
public class LoggerUtil {
    private static Logger logger;
    public static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getAnonymousLogger();
            logger.setUseParentHandlers(false);

            ConsoleHandler handler = new ConsoleHandler();
            handler.setFormatter(new LogFormatter());
            handler.setLevel(Level.FINEST);
            logger.addHandler(handler);
            logger.setLevel(Level.FINEST);
        }

        return logger;
    }

    public static void setLoggerLevel(Level level) {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new LogFormatter());
        handler.setLevel(level);
        logger.addHandler(handler);
        logger.setLevel(level);
    }

    public static void raw(Object message) {
        getLogger().log(Level.FINE, String.valueOf(message), new Object[] { false, "" } );
    }

    public static void log(Object message) {
        getLogger().log(Level.FINE, String.valueOf(message), new Object[] { false });
    }

    public static void log(Object message, String prefix) {
        getLogger().log(Level.FINE, String.valueOf(message), new Object[] { false, prefix } );
    }

    public static void logInline(Object message) {
        getLogger().log(Level.FINE, String.valueOf(message), new Object[] { true });
    }

    public static void logInline(Object message, String prefix) {
        getLogger().log(Level.FINE, String.valueOf(message), new Object[] { true, prefix });
    }

    public static void info(Object message) {
        getLogger().log(Level.INFO, String.valueOf(message), new Object[] { false });
    }

    public static void info(Object message, String prefix) {
        getLogger().log(Level.INFO, String.valueOf(message), new Object[] { false, prefix } );
    }

    public static void infoInline(Object message) {
        getLogger().log(Level.INFO, String.valueOf(message), new Object[] { true });
    }

    public static void infoInline(Object message, String prefix) {
        getLogger().log(Level.INFO, String.valueOf(message), new Object[] { true, prefix });
    }

    public static void warn(Object message) {
        getLogger().log(Level.WARNING, String.valueOf(message), new Object[] { false });
    }

    public static void warn(Object message, String prefix) {
        getLogger().log(Level.WARNING, String.valueOf(message), new Object[] { false, prefix } );
    }

    public static void warnInline(Object message) {
        getLogger().log(Level.WARNING, String.valueOf(message), new Object[] { true });
    }

    public static void warnInline(Object message, String prefix) {
        getLogger().log(Level.WARNING, String.valueOf(message), new Object[] { true, prefix });
    }

    public static void severe(Object message) {
        getLogger().log(Level.SEVERE, String.valueOf(message), new Object[] { false });
    }

    public static void severe(Object message, String prefix) {
        getLogger().log(Level.SEVERE, String.valueOf(message), new Object[] { false, prefix } );
    }

    public static void severeInline(Object message) {
        getLogger().log(Level.SEVERE, String.valueOf(message), new Object[] { true });
    }

    public static void severeInline(Object message, String prefix) {
        getLogger().log(Level.SEVERE, String.valueOf(message), new Object[] { true, prefix });
    }



    public static class LogFormatter extends Formatter {
        private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // ANSI Escape codes
        public static final String RESET = "\u001B[0m";
        public static final String RESET_FG = "\u001B[39m";
        public static final String RESET_BG = "\u001B[49m";
        public static final String BLACK = "\u001B[30m";
        public static final String RED = "\u001B[31m";
        public static final String BRIGHT_RED = "\u001B[38;5;9m";
        public static final String BG_BRIGHT_RED = "\u001B[48;5;196m";
        public static final String GREEN = "\u001B[38;5;10m";
        public static final String DARK_GREEN = "\u001B[38;5;40m";
        public static final String YELLOW = "\u001B[38;5;11m";
        public static final String GOLD = "\u001B[38;5;3m";
        public static final String BLUE = "\u001B[34m";
        public static final String PURPLE = "\u001B[35m";
        public static final String CYAN = "\u001B[36m";
        public static final String LIGHT_GRAY = "\u001B[38;5;250m";
        public static final String WHITE = "\u001B[97m";

        public static final String BOLD = "\u001B[1m";
        public static final String INV_BOLD = "\u001B[22m";
        public static final String ITALIC = "\u001B[3m";
        public static final String INV_ITALIC = "\u001B[23m";

        private static final Pattern HIGHLIGHT_START = Pattern.compile("\\$HL");
        private static final Pattern HIGHLIGHT_END = Pattern.compile("\\$");

        @Override
        public String format(LogRecord logRecord) {
            StringBuilder builder = new StringBuilder();
            builder.append(LIGHT_GRAY).append(df.format(new Date(logRecord.getMillis()))).append(" - ");
            builder.append(logRecord.getLevel().intValue() >= Level.INFO.intValue() ? WHITE : LIGHT_GRAY)
                    .append("[").append(Thread.currentThread().getStackTrace()[8].getClassName()).append(".").append(Thread.currentThread().getStackTrace()[8].getMethodName())
                    .append("(").append(Thread.currentThread().getStackTrace()[8].getFileName()).append(":")
                    .append(Thread.currentThread().getStackTrace()[8].getLineNumber()).append(")]");
            builder.append(WHITE).append(messageLayout(logRecord));

            String color = levelColor(logRecord.getLevel());

            String message = formatMessage(logRecord);
            message = HIGHLIGHT_START.matcher(message).replaceAll(highlight(logRecord.getLevel(), false));
            message = HIGHLIGHT_END.matcher(message).replaceAll(highlight(logRecord.getLevel(), true));

            String level = (logRecord.getParameters() != null && logRecord.getParameters().length >= 2) ? (String) logRecord.getParameters()[1] : logRecord.getLevel().getLocalizedName();
            if (logRecord.getLevel() == Level.SEVERE) {
                builder.append(BG_BRIGHT_RED + BOLD + BLACK).append(level).append(RESET_BG + INV_BOLD).append(color).append(Objects.equals(level, "") ? "" : ": ");
            } else {
                builder.append(color).append(level).append(Objects.equals(level, "") ? "" : ": ");
            }
            builder.append(message);
            builder.append(RESET).append("\n");
            return builder.toString();
        }

        private String messageLayout(LogRecord logRecord) {
            return (!((boolean) logRecord.getParameters()[0]) && logRecord.getLevel().intValue() >= Level.INFO.intValue()) ? " (Thread: " + logRecord.getLongThreadID() + ")\n" : " >> ";
        }

        private String highlight(Level level, boolean inverse) {
            if (inverse) {
                return level.intValue() >= Level.FINE.intValue() ? INV_BOLD + levelColor(level) : levelColor(level);
            } else {
                return level.intValue() >= Level.FINE.intValue() ? BOLD + highlightColor(level) : highlightColor(level);
            }
        }

        private String levelColor(Level level) {
            if (level == Level.FINER || level == Level.FINEST) {
                return LIGHT_GRAY;
            } else if (level == Level.INFO) {
                return DARK_GREEN;
            } else if (level == Level.WARNING) {
                return YELLOW;
            } else if (level == Level.SEVERE) {
                return RED;
            } else {
                return WHITE;
            }
        }

        private String highlightColor(Level level) {
            if (level == Level.FINER || level == Level.FINEST) {
                return WHITE;
            } else if (level == Level.INFO) {
                return GREEN;
            } else if (level == Level.WARNING) {
                return GOLD;
            } else if (level == Level.SEVERE) {
                return BRIGHT_RED;
            } else {
                return CYAN;
            }
        }
    }
}
