package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import nearlmod.patches.CurseRelicPatch;
import nearlmod.powers.AttackUpPower;

import java.util.Iterator;

public class TangibleTerror extends CustomRelic {

    public static final String ID = "nearlmod:TangibleTerror";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/nearlmod/images/relics/tangibleterror.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/nearlmod/images/relics/tangibleterror_p.png");

    public TangibleTerror() {
        super(ID, IMG, IMG_OUTLINE, CurseRelicPatch.CURSE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TangibleTerror();
    }
    public void atBattleStart() {
        flash();
        for (AbstractMonster m:AbstractDungeon.getMonsters().monsters) {
            this.addToTop(new RelicAboveCreatureAction(m, this));
            m.addPower(new AttackUpPower(m, 10));
        }

        AbstractDungeon.onModifyPower();
    }

    @Override
    public void onSpawnMonster(AbstractMonster monster) {
        flash();
        monster.addPower(new AttackUpPower(monster,10));

        AbstractDungeon.onModifyPower();
    }
}
