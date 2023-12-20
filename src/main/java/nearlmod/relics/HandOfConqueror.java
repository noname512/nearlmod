package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nearlmod.powers.AttackUpPower;

import static java.lang.Math.min;

public class HandOfConqueror extends CustomRelic {

    public static final String ID = "nearlmod:HandOfConqueror";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/handofconqueror.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/handofconqueror_p.png");
    public int AwakenPatch = 0;
    public HandOfConqueror() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.RARE, LandingSound.FLAT);
        counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
        if (counter > 0) {
            flash();
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new ApplyPowerAction(p, p, new AttackUpPower(p, counter)));
        }
        AwakenPatch = 0;
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (!m.halfDead && !m.hasPower(MinionPower.POWER_ID)) {
            if (m instanceof AwakenedOne) {
                if (AwakenPatch == 1) {
                    return;
                }
                else {
                    AwakenPatch = 1;
                }
            }
            int add_num = min(2, 99 - counter);
            if (counter < 99) {
                flash();
                counter += add_num;
                AbstractPlayer p = AbstractDungeon.player;
                addToBot(new ApplyPowerAction(p, p, new AttackUpPower(p, add_num)));
            }
        }
    }

    public boolean canSpawn() {
        return AbstractDungeon.floorNum <= 40;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HandOfConqueror();
    }
}
