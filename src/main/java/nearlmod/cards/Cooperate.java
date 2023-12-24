package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.NLMOD;
import nearlmod.actions.UseLightAction;
import nearlmod.orbs.Blemishine;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.LightPower;
import nearlmod.stances.AtkStance;

public class Cooperate extends AbstractNearlCard {
    public static final String ID = "nearlmod:Cooperate";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/gloriouskazimierz.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 8;
    private static final int LIGHT_GAIN = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int UPGRADE_PLUS_LIGHT = 2;

    public Cooperate() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = LIGHT_GAIN;
        tags.add(NearlTags.IS_GAIN_LIGHT);
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Blemishine.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.LIGHTNING));
        if (extraTriggered()) {
            addToBot(new ApplyPowerAction(p, p, new LightPower(p, magicNumber)));
            if (p.stance.ID.equals(AtkStance.STANCE_ID)) {
                addToBot(new UseLightAction(p, m));
            } else {
                addToBot(new UseLightAction(p, null));
            }
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        magicNumber = baseMagicNumber;
        for (AbstractOrb orb : AbstractDungeon.player.orbs) {
            if (orb instanceof Blemishine) {
                magicNumber += ((Blemishine) orb).getTrustAmount();
            }
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        magicNumber = baseMagicNumber;
        for (AbstractOrb orb : AbstractDungeon.player.orbs) {
            if (orb instanceof Blemishine) {
                magicNumber += ((Blemishine) orb).getTrustAmount();
            }
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Cooperate();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_LIGHT);
        }
    }
}
