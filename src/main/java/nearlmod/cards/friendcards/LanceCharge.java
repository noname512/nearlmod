package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.patches.AbstractCardEnum;

import static nearlmod.patches.NearlTags.IS_KNIGHT_CARD;

public class LanceCharge extends AbstractFriendCard {
    public static final String ID = "nearlmod:LanceCharge";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/lancecharge.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 10;
    private static final int UPGRADE_PLUS_DMG = 5;
    private static final int DECREASE_STRENGTH = 99;

    public LanceCharge() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.ENEMY, "nearlmod:Wildmane");
        magicNumber = baseMagicNumber = ATTACK_DMG;
        tags.add(IS_KNIGHT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, magicNumber)));
        if (m.getIntentBaseDmg() >= 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new StrengthPower(m, -DECREASE_STRENGTH), -DECREASE_STRENGTH));
            if (!m.hasPower("Artifact")) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new GainStrengthPower(m, DECREASE_STRENGTH), DECREASE_STRENGTH));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LanceCharge();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DMG);
        }
    }
}
