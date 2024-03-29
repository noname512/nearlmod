package nearlmod.cards;

import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.actions.UseLightAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.ExsanguinationPower;
import nearlmod.stances.AtkStance;

import static java.lang.Integer.min;

public class KnightCrest extends AbstractNearlCard {
    public static final String ID = "nearlmod:KnightCrest";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/knightcrest.png";
    private static final int COST = 0;
    private static final int ATTACK_DMG = 3;
    private static final int UPGRADE_PLUS_DMG = 2;

    public KnightCrest() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        tags.add(NearlTags.IS_USE_LIGHT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!p.stance.ID.equals(AtkStance.STANCE_ID)) {
            addToBot(new ChangeStanceAction(new AtkStance()));
        }
        addToBot(new DamageAction(m, new DamageInfo(p, damage, this.damageType), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new UseLightAction(p, m));
    }

    int calc() {
        int num =  AtkStance.incNum + AtkStance.atkInc;
        if (AbstractDungeon.player.hasPower(ExsanguinationPower.POWER_ID)) {
            int strength = 0;
            if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)) {
                strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount;
            }
            num = min(num, -strength);
        }
        return num;
    }

    private void preUpd(int atk) {
        if (!AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID)) {
            this.baseDamage += atk;
        }
    }

    private void postUpd(int atk) {
        if (!AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID)) {
            this.baseDamage -= atk;
            isDamageModified = (baseDamage != damage);
        }
    }

    @Override
    public void applyPowers() {
        int atk = calc();
        preUpd(atk);
        super.applyPowers();
        postUpd(atk);
    }
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int atk = calc();
        preUpd(atk);
        super.calculateCardDamage(mo);
        postUpd(atk);
    }
    @Override
    public AbstractCard makeCopy() {
        return new KnightCrest();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
