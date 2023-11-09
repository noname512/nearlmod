package nearlmod.cards;

import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
import com.jcraft.jorbis.Block;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.BodySlam;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.MagicFlower;
import jdk.jfr.Description;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class AllinOne extends AbstractNearlCard {
    public static final String ID = "nearlmod:AllinOne";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/allinone.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 4;
    private static final int BLOCK_AMT = 4;

    public AllinOne() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        block = baseBlock = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        if (p.stance.ID.equals(AtkStance.STANCE_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new DefStance()));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new AtkStance()));
        }
    }

    public void applyPowers() {
        super.applyPowers();
        if (upgraded) {
            AbstractPower tempDex = AbstractDungeon.player.getPower("Dexterity");
            AbstractPower tempStr = AbstractDungeon.player.getPower("Strength");
            int dex = 0, str = 0;
            if (tempDex != null) {
                dex = tempDex.amount;
            }
            if (tempStr != null) {
                str = tempStr.amount;
            }
            damage = damage + dex;
            if (damage < 0) {
                damage = 0;
            }
            if (damage != baseDamage) {
                isDamageModified = true;
            }
            block = block + str;
            if (block < 0) {
                block = 0;
            }
            if (block != baseBlock) {
                isBlockModified = true;
            }
        }
    }
    @Override
    public AbstractCard makeCopy() {
        return new AllinOne();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            // TODO: 怎么吃两边buff啊（绿字）
        }
    }
}
