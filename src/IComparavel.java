public interface IComparavel {
    /**
     * compara dois objetos que respondem a essa interface
     * @param outro um segundo objeto que sera comparado
     * @return true se for maior
     */
    boolean maiorQue(IComparavel outro);
    /**
     * compara dois objetos que respondem a essa interface
     * @param outro um segundo objeto que sera comparado
     * @return true se for menor
     */
    boolean menorQue(IComparavel outro);
}
