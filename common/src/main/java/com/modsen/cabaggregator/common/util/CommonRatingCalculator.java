package com.modsen.cabaggregator.common.util;

import java.text.DecimalFormat;
import java.util.stream.DoubleStream;

public final class CommonRatingCalculator {

    private CommonRatingCalculator() {
    }

    public static double getAverage(DoubleStream stream) {
        return Double.parseDouble(
                new DecimalFormat(GlobalConstants.DECIMAL_FORMAT_PATTERN)
                        .format(stream.average()
                                .orElse(GlobalConstants.DEFAULT_SCORE)
                        )
        );
    }

}
