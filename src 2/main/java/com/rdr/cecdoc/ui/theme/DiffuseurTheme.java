package com.rdr.cecdoc.ui.theme;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

public final class DiffuseurTheme {
    private final SelecteurTheme selector;
    private final List<EcouteurTheme> listeners;
    private final AtomicReference<TokensTheme> themeActuel;

    public DiffuseurTheme(SelecteurTheme selector, ModeTheme initialMode) {
        this.selector = Objects.requireNonNull(selector, "selector");
        this.listeners = new CopyOnWriteArrayList<>();
        this.themeActuel = new AtomicReference<>(selector.selectionner(initialMode));
    }

    public TokensTheme themeActuel() {
        return themeActuel.get();
    }

    public void publierMode(ModeTheme mode) {
        TokensTheme next = selector.selectionner(mode);
        themeActuel.set(next);
        notifierEcouteurs(next);
    }

    public void publierChoixPronom(boolean pronomNeutre) {
        TokensTheme next = selector.selectionnerPourPronom(pronomNeutre);
        themeActuel.set(next);
        notifierEcouteurs(next);
    }

    public void ajouterEcouteur(EcouteurTheme listener) {
        listeners.add(Objects.requireNonNull(listener, "listener"));
    }

    private void notifierEcouteurs(TokensTheme next) {
        for (EcouteurTheme listener : listeners) {
            listener.themeChange(next);
        }
    }
}
