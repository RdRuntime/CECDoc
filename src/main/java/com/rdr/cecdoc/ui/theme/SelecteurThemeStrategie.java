package com.rdr.cecdoc.ui.theme;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;


public final class SelecteurThemeStrategie implements SelecteurTheme {
    private final Map<ModeTheme, StrategieTheme> strategies;


    public SelecteurThemeStrategie(Iterable<StrategieTheme> strategies) {
        this.strategies = new EnumMap<>(ModeTheme.class);
        for (StrategieTheme strategy : strategies) {
            StrategieTheme nonNull = Objects.requireNonNull(strategy, "strategy");
            this.strategies.put(nonNull.mode(), nonNull);
        }
        if (!this.strategies.containsKey(ModeTheme.TRANS) || !this.strategies.containsKey(ModeTheme.NON_BINAIRE) || !this.strategies.containsKey(ModeTheme.LESBIEN) || !this.strategies.containsKey(ModeTheme.INTERSEXE) || !this.strategies.containsKey(ModeTheme.RAINBOW)) {
            throw new IllegalArgumentException("Tous les thèmes doivent être enregistrés.");
        }
    }


    @Override
    public TokensTheme selectionner(ModeTheme mode) {
        StrategieTheme strategy = strategies.get(Objects.requireNonNull(mode, "mode"));
        if (strategy == null) {
            throw new IllegalArgumentException("Mode de thème non-supporté: " + mode);
        }
        return strategy.creer();
    }


    @Override
    public TokensTheme selectionnerPourPronom(boolean pronomNeutre) {
        return selectionner(pronomNeutre ? ModeTheme.NON_BINAIRE : ModeTheme.TRANS);
    }
}
