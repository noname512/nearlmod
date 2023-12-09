package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.LightPower;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class LightCascade extends AbstractNearlCard {
    public static final String ID = "nearlmod:LightCascade";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/lightcascade.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public LightCascade() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        tags.add(NearlTags.IS_USE_LIGHT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = LightPower.getAmount();
        LightPower.changeToShadow(true);
        if (p.stance.ID.equals(DefStance.STANCE_ID))
            addToBot(new ChangeStanceAction(new AtkStance()));
        addToBot(new PureDamageAllEnemiesAction(p, amount, this.name, AbstractGameAction.AttackEffect.NONE, DamageInfo.DamageType.THORNS));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LightCascade();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
